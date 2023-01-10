package me.interview.RealTimeMarkToMarket.dao;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "OTC_OPTION_CONTRACT")
public class OtcOptionContract {
    @Id
    @Column(name = "contract_num")
    private String contractNum;

    @ManyToOne
    @JoinColumn(name = "underlier_id")
    private Underlier underlier;

    @Column(name = "option_product")
    private String optionProduct;

    @Column(name = "notional")
    private BigDecimal notional;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "maturity_date")
    private LocalDate maturityDate;

    @Column(name = "initial_price")
    private BigDecimal initialPrice;

    @Column(name = "exercise_price")
    private BigDecimal exercisePrice;

    @Column(name = "participate_ratio")
    private BigDecimal participateRatio;

    @Column(name = "status")
    private char status;

    public OtcOptionContract(){}

    public OtcOptionContract(String contractNum, Underlier underlier, String optionProduct, char status) {
        this.contractNum = contractNum;
        this.underlier = underlier;
        this.optionProduct = optionProduct;
        this.status = status;
    }
}
