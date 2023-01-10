package me.interview.RealTimeMarkToMarket.service;

import me.interview.RealTimeMarkToMarket.dto.*;
import me.interview.RealTimeMarkToMarket.repository.HedgePositionRepository;
import me.interview.RealTimeMarkToMarket.repository.OtcOptionContractRepository;
import me.interview.RealTimeMarkToMarket.repository.OtcOptionPortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MarkToMarketService {
    @Autowired
    private OtcOptionPortfolioRepository portfolioRepository;

    @Autowired
    private OtcOptionContractRepository optionRepository;

    @Autowired
    private HedgePositionRepository hedgeRepository;

    @Autowired
    private PricerService pricerService;

    /**
     * Get all OTC option contracts and hedge futures contracts. Transfer them into DTO Contract.
     * Then call the pricer method to calculate the PnL and greek numbers
     * @return list of Contract
     */
    private List<Contract> getContracts() {
        List<Contract> res = optionRepository.findAll().stream()
                        .filter(ooc->ooc.getStatus()=='1')
                        .map(Contract::fromDAO)
                        .collect(Collectors.toList());
        res.addAll(hedgeRepository.findAll()
                                  .stream()
                                  .map(Contract::fromDAO)
                                  .collect(Collectors.toList()));

        PricerResult[] pricerResults = pricerService.pricer(res.toArray(new Contract[res.size()]));

        for (int i = 0; i < pricerResults.length; i++) {
            Contract contract = res.get(i);
            PricerResult pr = pricerResults[i];
            contract.setDailyPnL(pr.getDailyPnL());
            contract.setDelta(pr.getDelta());
            contract.setGamma(pr.getGamma());
            contract.setVega(pr.getVega());
        }

        return res;
    }

    private List<Portfolio> getPortfolios() {
        return portfolioRepository.findAll()
                                  .stream()
                                  .map(Portfolio::fromDAO)
                                  .collect(Collectors.toList());
    }

    /**
     * Get mark to market indicators aggregated by AggregateQuery keys. Query is by default pivoted by dimensions of
     * Portfolio -> Underlier Category -> Underlier -> Product. The query can also be pivoted by one or multiple
     * of the four dimensions.
     * @param query Specify the aggregate dimensions for query
     * @return Dimension list. Dimension is a tree structure. Each dimension stands for
     */
    public List<Dimension> getAggregateMarkToMarketIndicators(AggregateQuery query) {
        List<Contract> contracts = this.getContracts();
        List<Portfolio> portfolios = this.getPortfolios();
        List<Dimension> dimensions = contracts.stream()
                                              .map(Dimension::fromContract)
                                              .collect(Collectors.toList());

        AggregateQuery.AggregateKey finalAggKey = query.getFinalAggregateKey();

        if (query.isQPortfolio()) {
            dimensions = aggregate(dimensions, portfolios, AggregateQuery.AggregateKey.PORTFOLIO, finalAggKey);
        }

        if (query.isQCategory()) {
            dimensions = aggregate(dimensions, portfolios, AggregateQuery.AggregateKey.CATEGORY, finalAggKey);
        }

        if (query.isQUnderlier()) {
            dimensions = aggregate(dimensions, portfolios, AggregateQuery.AggregateKey.UNDERLIER, finalAggKey);
        }

        if (query.isQProduct()) {
            dimensions = aggregate(dimensions, portfolios, AggregateQuery.AggregateKey.PRODUCT, finalAggKey);
        }

        return dimensions;
    }

    private List<Dimension> aggregate(List<Dimension> dimensions, List<Portfolio> portfolios,
                                      AggregateQuery.AggregateKey aggregateKey,
                                      AggregateQuery.AggregateKey finalAggKey) {
        // leaf dimension
        if (dimensions.get(0).getSubDimensions() == null) {
            Map<String, List<Dimension>> intermediate = new HashMap<>();
            switch (aggregateKey) {
                case PORTFOLIO:
                    Map<String, List<Dimension>> contractNumMap = dimensions.stream()
                                             .collect(Collectors.groupingBy(Dimension::getContractNum));
                    for (Portfolio portfolio : portfolios) {
                        List<Dimension> val = new ArrayList<>();
                        for (Contract contract : portfolio.getContracts()) {
                            List<Dimension> dl = contractNumMap.get(contract.getContractNum());
                            if (dl != null) val.addAll(dl);
                        }
                        if (val.size() > 0) intermediate.put(portfolio.getName(), val);
                    }
                    intermediate.put("全量合约", dimensions);
                    break;
                case CATEGORY:
                    intermediate = dimensions.stream().collect(Collectors.groupingBy(Dimension::getCategoryName));
                    break;
                case UNDERLIER:
                    intermediate = dimensions.stream().collect(Collectors.groupingBy(Dimension::getUnderlierName));
                    break;
                case PRODUCT:
                    intermediate = dimensions.stream().collect(Collectors.groupingBy(Dimension::getProductName));
                    break;
            }
            List<Dimension> res = new ArrayList<>();
            // sum the Greek numbers based on the aggregate key
            for (String key : intermediate.keySet()) {
                Dimension dimension = new Dimension();
                dimension.setName(key);
                dimension.setGamma(intermediate.get(key).stream()
                        .map(Dimension::getGamma)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                dimension.setDelta(intermediate.get(key).stream()
                        .map(Dimension::getDelta)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                dimension.setVega(intermediate.get(key).stream()
                        .map(Dimension::getVega)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                dimension.setDailyPnL(intermediate.get(key).stream()
                        .map(Dimension::getDailyPnL)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                if (!aggregateKey.equals(finalAggKey)) {
                    // product is the leaf aggregate dimension, no sub dimension
                    dimension.setSubDimensions(intermediate.get(key));
                }
                res.add(dimension);
            }
            return res;
        } else {
            for (Dimension dimension : dimensions) {
                List<Dimension> sub = aggregate(dimension.getSubDimensions(), portfolios, aggregateKey, finalAggKey);
                dimension.setSubDimensions(sub);
            }

            return dimensions;
        }
    }
}
