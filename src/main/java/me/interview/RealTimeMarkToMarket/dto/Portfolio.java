package me.interview.RealTimeMarkToMarket.dto;

import lombok.Data;
import me.interview.RealTimeMarkToMarket.dao.OtcOptionPortfolio;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class Portfolio {
    private String name;
    private List<Contract> contracts;

    public static Portfolio fromDAO(OtcOptionPortfolio otcOptionPortfolio) {
        Portfolio portfolio = new Portfolio();
        portfolio.setName(otcOptionPortfolio.getName());
        portfolio.setContracts(otcOptionPortfolio.getContractList()
                                                 .stream()
                                                 .map(Contract::fromDAO)
                                                 .collect(Collectors.toList()));
        return portfolio;
    }
}
