package me.interview.RealTimeMarkToMarket.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PricerResult {
    private BigDecimal delta;
    private BigDecimal gamma;
    private BigDecimal vega;
    private BigDecimal dailyPnL;

    public PricerResult(BigDecimal dailyPnL, BigDecimal delta, BigDecimal gamma, BigDecimal vega) {
        this.dailyPnL = dailyPnL;
        this.delta = delta;
        this.gamma = gamma;
        this.vega = vega;
    }

    public PricerResult(){}
}
