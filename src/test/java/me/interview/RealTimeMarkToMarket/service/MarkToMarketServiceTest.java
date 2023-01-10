package me.interview.RealTimeMarkToMarket.service;

import me.interview.RealTimeMarkToMarket.dao.*;
import me.interview.RealTimeMarkToMarket.dto.*;
import me.interview.RealTimeMarkToMarket.repository.HedgePositionRepository;
import me.interview.RealTimeMarkToMarket.repository.OtcOptionContractRepository;
import me.interview.RealTimeMarkToMarket.repository.OtcOptionPortfolioRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MarkToMarketServiceTest {
    private static final String IF300 = "沪深300品种";
    private static final String IC500 = "中证500品种";
    private static final String ALL_CONTRACTS = "全量合约";
    private static final String HEDGE_FUTURE = "场内期货";

    private static final String PORTFOLIO_A = "投资组合A";
    private static final String PORTFOLIO_B = "投资组合B";
    private static final String SNOWBALL = "非保本雪球";
    private static final String BINARY = "二元";
    @InjectMocks
    private MarkToMarketService markToMarketService;
    @Mock
    private OtcOptionContractRepository mockOptionRepo;
    @Mock
    private HedgePositionRepository mockHedgeRepo;
    @Mock
    private OtcOptionPortfolioRepository mockPortfolioRepo;
    @Mock
    private PricerService pricerService;

    private static Underlier ifIndex = new Underlier();
    private static Underlier ifFuture = new Underlier();
    private static Underlier icIndex = new Underlier();
    private static Underlier icFuture = new Underlier();
    private static UnderlierCategory IF = new UnderlierCategory();
    private static UnderlierCategory IC = new UnderlierCategory();
    private static List<OtcOptionContract> optionContracts = new ArrayList<>();
    private static List<HedgePosition> hedgePositions = new ArrayList<>();
    private static List<OtcOptionPortfolio> portfolios = new ArrayList<>();

    private static void initUnderliers() {
        IF.setId(1);
        IF.setName("沪深300品种");
        IC.setId(2);
        IC.setName("中证500品种");

        ifIndex.setId(1);
        ifIndex.setCode("1B0300.SH");
        ifIndex.setUnderlierCategory(IF);

        ifFuture.setId(2);
        ifFuture.setCode("IF2212.CFE");
        ifFuture.setUnderlierCategory(IF);

        icIndex.setId(3);
        icIndex.setCode("000905.SH");
        icIndex.setUnderlierCategory(IC);

        icFuture.setId(4);
        icFuture.setCode("IC2212.CFE");
        icFuture.setUnderlierCategory(IC);
    }

    private static void initOptionContracts() {
        OtcOptionContract contract1 = new OtcOptionContract("OTC202301090001", ifIndex, "非保本雪球", '1');
        OtcOptionContract contract2 = new OtcOptionContract("OTC202301090002", icIndex, "二元", '1');
        OtcOptionContract contract3 = new OtcOptionContract("OTC202301090003", ifIndex, "二元", '1');
        OtcOptionContract contract4 = new OtcOptionContract("OTC202301090004", icIndex, "非保本雪球", '0');

//        contract1.setPortfolioList(Arrays.asList(pA, pB));
//        contract2.setPortfolioList(Arrays.asList(pA, pB));
//        contract3.setPortfolioList(Arrays.asList(pB));
//        contract4.setPortfolioList(Arrays.asList(pC));

        optionContracts.add(contract1);
        optionContracts.add(contract2);
        optionContracts.add(contract3);
        optionContracts.add(contract4);
    }

    private static void initPortfolios() {
        OtcOptionPortfolio pA = new OtcOptionPortfolio(1, "投资组合A");
        OtcOptionPortfolio pB = new OtcOptionPortfolio(2, "投资组合B");
        OtcOptionPortfolio pC = new OtcOptionPortfolio(3, "投资组合C");

        pA.setContractList(Arrays.asList(optionContracts.get(0), optionContracts.get(1)));
        pB.setContractList(Arrays.asList(optionContracts.get(0), optionContracts.get(1), optionContracts.get(2)));
        pC.setContractList(Arrays.asList(optionContracts.get(3)));

        portfolios.add(pA);
        portfolios.add(pB);
        portfolios.add(pC);
    }

    private static void initHedgePositions() {
        HedgePosition h1 = new HedgePosition("HDG202301090001", ifFuture);
        HedgePosition h2 = new HedgePosition("HDG202301090002", icFuture);
        hedgePositions.add(h1);
        hedgePositions.add(h2);
    }

    static {
        initUnderliers();
        initOptionContracts();
        initPortfolios();
        initHedgePositions();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void initMockMethods() {
        Mockito.when(mockOptionRepo.findAll()).thenReturn(optionContracts);
        Mockito.when(mockHedgeRepo.findAll()).thenReturn(hedgePositions);
        Mockito.when(mockPortfolioRepo.findAll()).thenReturn(portfolios);
        //pA otc0001, pB otc0001, pA otc0002, pB otc0002, pB otc0003, hdg0001, hdg0002
        PricerResult[] mockPricerResults = new PricerResult[5];
        Arrays.fill(mockPricerResults, new PricerResult(BigDecimal.valueOf(1000.00),
                BigDecimal.valueOf(0.8), BigDecimal.valueOf(100.00), BigDecimal.valueOf(200.00)));
        Mockito.when(pricerService.pricer(Mockito.any())).thenReturn(mockPricerResults);
    }

    @Test
    public void testGetAggregateMarkToMarketIndicatorsByPortfolio() {
        AggregateQuery query = new AggregateQuery();
        query.setQPortfolio(true);

        List<Dimension> res = markToMarketService.getAggregateMarkToMarketIndicators(query);
        //all, pA, pB three dimensions
        assertThat(res.size(), is(3));
        // otc0001 - 0003, hedge0001-0002. in total delta is 0.8*5
        assertThat(res.stream().filter(d -> ALL_CONTRACTS.equals(d.getName())).findFirst().get().getDelta(), is(BigDecimal.valueOf(4.0)));
        // otc0001 & otc0002. in total delta is 0.8*2
        assertThat(res.stream().filter(d -> "投资组合A".equals(d.getName())).findFirst().get().getDelta(), is(BigDecimal.valueOf(1.6)));
        // otc0001-0003. in total delta is 0.8 * 3
        assertThat(res.stream().filter(d -> "投资组合B".equals(d.getName())).findFirst().get().getDelta(), is(BigDecimal.valueOf(2.4)));
    }

    @Test
    public void testGetAggregateMarkToMarketIndicatorsByCategory() {
        AggregateQuery query = new AggregateQuery();
        query.setQCategory(true);

        // IF: otc0001,otc0003,hedge0001 IC: otc0002, hedge0002
        List<Dimension> res = markToMarketService.getAggregateMarkToMarketIndicators(query);
        assertThat(res.size(), is(2));
        assertThat(res.stream().filter(d->"沪深300品种".equals(d.getName())).findFirst().get().getDelta(),
                is(BigDecimal.valueOf(2.4)));
        assertThat(res.stream().filter(d->"中证500品种".equals(d.getName())).findFirst().get().getDelta(),
                is(BigDecimal.valueOf(1.6)));
    }

    @Test
    public void testGetAggregateMarkToMarketIndicatorsByUnderlier() {
        AggregateQuery query = new AggregateQuery();
        query.setQUnderlier(true);

        //1B0300: otc0001, 0tc0003 , IF2212: hedge0001, 000905: otc0002, IC2212:hedge0002
        List<Dimension> res = markToMarketService.getAggregateMarkToMarketIndicators(query);
        assertThat(res.size(), is(4));
        assertThat(res.stream().filter(d->"1B0300.SH".equals(d.getName())).findFirst().get().getDelta(),
                is(BigDecimal.valueOf(1.6)));
        assertThat(res.stream().filter(d->"000905.SH".equals(d.getName())).findFirst().get().getDelta(),
                is(BigDecimal.valueOf(0.8)));
        assertThat(res.stream().filter(d->"IF2212.CFE".equals(d.getName())).findFirst().get().getDelta(),
                is(BigDecimal.valueOf(0.8)));
        assertThat(res.stream().filter(d->"IC2212.CFE".equals(d.getName())).findFirst().get().getDelta(),
                is(BigDecimal.valueOf(0.8)));
    }

    @Test
    public void testGetAggregateMarkToMarketIndicatorsByProduct() {
        AggregateQuery query = new AggregateQuery();
        query.setQProduct(true);

        // snowball: otc0001, binary: otc0002, otc0003, futures: hedge0001,hedge0002
        List<Dimension> res = markToMarketService.getAggregateMarkToMarketIndicators(query);
        assertThat(res.size(), is(3));
        assertThat(res.stream().filter(d->"非保本雪球".equals(d.getName())).findFirst().get().getDelta(),
                is(BigDecimal.valueOf(0.8)));
        assertThat(res.stream().filter(d->"二元".equals(d.getName())).findFirst().get().getDelta(),
                is(BigDecimal.valueOf(1.6)));
        assertThat(res.stream().filter(d->"场内期货".equals(d.getName())).findFirst().get().getDelta(),
                is(BigDecimal.valueOf(1.6)));
    }

    @Test
    public void testGetAggregateMarkToMarketIndicatorsByAll() {
        AggregateQuery query = new AggregateQuery();
        query.setQPortfolio(true);
        query.setQCategory(true);
        query.setQUnderlier(true);
        query.setQProduct(true);

        /**
         * 全量合约
         *  - 沪深300品种
         *   - 1B0300.SH
         *    - 非保本雪球: otc0001
         *    - 二元: otc0003
         *   - IF2212.CFE
         *    - 场内期货: hdg0001
         *  - 中证500品种
         *   - 000905.SH
         *    - 二元: otc0002
         *   - IC2212.CFE
         *    - 场内期货: hdg0002
         * 投资组合A
         *  - 沪深300品种
         *   - 1B0300.SH
         *    - 非保本雪球: otc0001
         *  - 中证500品种
         *   - 000905.SH
         *    - 二元: otc0001
         * 投资组合B
         *  - 沪深300品种
         *   - 1B0300.SH
         *    - 非保本雪球: otc0001
         *    - 二元: otc0003
         *  - 中证500品种
         *   - 000905.SH
         *    - 二元: otc0002
         */
        List<Dimension> res = markToMarketService.getAggregateMarkToMarketIndicators(query);
        // first dimension - portfolio
        assertDimensionByDelta(res, 3, Arrays.asList(ALL_CONTRACTS, PORTFOLIO_A, PORTFOLIO_B),
                Arrays.asList(BigDecimal.valueOf(4.0), BigDecimal.valueOf(1.6), BigDecimal.valueOf(2.4)));
        // second dimension - underlier category
        List<Dimension> allCategories = res.stream().filter(d->ALL_CONTRACTS.equals(d.getName()))
                                           .findFirst().get().getSubDimensions();
        List<Dimension> paCategories = res.stream().filter(d->PORTFOLIO_A.equals(d.getName()))
                                          .findFirst().get().getSubDimensions();
        List<Dimension> pbCategories = res.stream().filter(d->PORTFOLIO_B.equals(d.getName()))
                                          .findFirst().get().getSubDimensions();
        assertDimensionByDelta(allCategories, 2, Arrays.asList(IF300, IC500),
                Arrays.asList(BigDecimal.valueOf(2.4), BigDecimal.valueOf(1.6)));
        assertDimensionByDelta(paCategories, 2, Arrays.asList(IF300, IC500),
                Arrays.asList(BigDecimal.valueOf(0.8), BigDecimal.valueOf(0.8)));
        assertDimensionByDelta(pbCategories, 2, Arrays.asList(IF300, IC500),
                Arrays.asList(BigDecimal.valueOf(1.6), BigDecimal.valueOf(0.8)));

        // third dimension - underlier
        List<Dimension> allCIF300 = allCategories.stream().filter(d->IF300.equals(d.getName()))
                                                 .findFirst().get().getSubDimensions();
        assertDimensionByDelta(allCIF300, 2, Arrays.asList("1B0300.SH", "IF2212.CFE"),
                Arrays.asList(BigDecimal.valueOf(1.6), BigDecimal.valueOf(0.8)));
        List<Dimension> allCIC500 = allCategories.stream().filter(d->IC500.equals(d.getName()))
                                                 .findFirst().get().getSubDimensions();
        assertDimensionByDelta(allCIC500, 2, Arrays.asList("000905.SH", "IC2212.CFE"),
                Arrays.asList(BigDecimal.valueOf(0.8), BigDecimal.valueOf(0.8)));

        List<Dimension> paCIF300 = paCategories.stream().filter(d->IF300.equals(d.getName()))
                                               .findFirst().get().getSubDimensions();
        assertDimensionByDelta(paCIF300, 1, Arrays.asList("1B0300.SH"), Arrays.asList(BigDecimal.valueOf(0.8)));
        List<Dimension> paCIC500 = paCategories.stream().filter(d->IC500.equals(d.getName()))
                                               .findFirst().get().getSubDimensions();
        assertDimensionByDelta(paCIC500, 1, Arrays.asList("000905.SH"), Arrays.asList(BigDecimal.valueOf(0.8)));

        List<Dimension> pbCIF300 = pbCategories.stream().filter(d->IF300.equals(d.getName()))
                                               .findFirst().get().getSubDimensions();
        assertDimensionByDelta(pbCIF300, 1, Arrays.asList("1B0300.SH"), Arrays.asList(BigDecimal.valueOf(1.6)));
        List<Dimension> pbCIC500 = pbCategories.stream().filter(d->IC500.equals(d.getName()))
                                               .findFirst().get().getSubDimensions();
        assertDimensionByDelta(pbCIC500, 1, Arrays.asList("000905.SH"), Arrays.asList(BigDecimal.valueOf(0.8)));

        // fourth dimension - product
        List<Dimension> acif1B0300 = allCIF300.stream().filter(d->"1B0300.SH".equals(d.getName()))
                                              .findFirst().get().getSubDimensions();
        assertDimensionByDelta(acif1B0300, 2, Arrays.asList(SNOWBALL, BINARY),
                Arrays.asList(BigDecimal.valueOf(0.8), BigDecimal.valueOf(0.8)));
        acif1B0300.forEach(d -> assertThat(d.getSubDimensions(), is(nullValue())));
        List<Dimension>  acifIF2212 = allCIF300.stream().filter(d->"IF2212.CFE".equals(d.getName()))
                                               .findFirst().get().getSubDimensions();
        assertDimensionByDelta(acifIF2212, 1, Arrays.asList(HEDGE_FUTURE), Arrays.asList(BigDecimal.valueOf(0.8)));
        acifIF2212.forEach(d -> assertThat(d.getSubDimensions(), is(nullValue())));
        List<Dimension> acic000905 = allCIC500.stream().filter(d->"000905.SH".equals(d.getName()))
                                              .findFirst().get().getSubDimensions();
        assertDimensionByDelta(acic000905, 1, Arrays.asList(BINARY), Arrays.asList(BigDecimal.valueOf(0.8)));
        acic000905.forEach(d -> assertThat(d.getSubDimensions(), is(nullValue())));
        List<Dimension> acicIC2212 = allCIC500.stream().filter(d->"IC2212.CFE".equals(d.getName()))
                                              .findFirst().get().getSubDimensions();
        assertDimensionByDelta(acicIC2212, 1, Arrays.asList(HEDGE_FUTURE), Arrays.asList(BigDecimal.valueOf(0.8)));
        acicIC2212.forEach(d -> assertThat(d.getSubDimensions(), is(nullValue())));
    }

    private void assertDimensionByDelta(List<Dimension> dimensions, int expectedSize,
                                        List<String> dimNames, List<BigDecimal> expectedDeltas) {
        assertThat(dimensions.size(), is(expectedSize));
        for (int i = 0; i < dimNames.size(); i++) {
            String dimName = dimNames.get(i);
            BigDecimal expectedDelta = expectedDeltas.get(i);
            BigDecimal actualDelta = dimensions.stream().filter(d->dimName.equals(d.getName())).findFirst().get().getDelta();
            assertThat(actualDelta, is(expectedDelta));
        }
    }
}
