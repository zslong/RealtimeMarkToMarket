package me.interview.RealTimeMarkToMarket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dimension {
    private String name;
    private String contractNum;
    private String portfolioName;
    private String categoryName;
    private String underlierName;
    private String productName;
    private BigDecimal delta;
    private BigDecimal gamma;
    private BigDecimal vega;
    private BigDecimal dailyPnL;
    private List<Dimension> subDimensions;

    public static Dimension fromContract(Contract contract) {
        Dimension dimension = new Dimension();
        dimension.name = contract.getContractNum();
        dimension.contractNum = contract.getContractNum();
        dimension.categoryName = contract.getUnderlier().getUnderlierCategory().getName();
        dimension.underlierName = contract.getUnderlier().getCode();
        dimension.productName = contract.getProduct();
        dimension.dailyPnL = contract.getDailyPnL();
        dimension.delta = contract.getDelta();
        dimension.gamma = contract.getGamma();
        dimension.vega = contract.getVega();
        return dimension;
    }
}
