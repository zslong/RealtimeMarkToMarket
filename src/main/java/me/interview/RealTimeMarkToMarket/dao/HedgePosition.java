package me.interview.RealTimeMarkToMarket.dao;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "HEDGE_POSITION")
public class HedgePosition {
    @Id
    @Column(name = "hedge_num")
    private String hedgeNum;

    @ManyToOne
    @JoinColumn(name = "underlier_id")
    private Underlier underlier;

    @Column(name = "net_position")
    private long netPosition;

    @Column(name = "market_value")
    private BigDecimal marketValue;

    @Column(name = "daily_pnl")
    private BigDecimal dailyPnL;

    @Column(name = "delta")
    private BigDecimal delta;

    public HedgePosition() {}

    public HedgePosition(String hedgeNum, Underlier underlier) {
        this.hedgeNum = hedgeNum;
        this.underlier = underlier;
    }
}
