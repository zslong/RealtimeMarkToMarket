package me.interview.RealTimeMarkToMarket.dto;

import lombok.Data;
import me.interview.RealTimeMarkToMarket.dao.HedgePosition;
import me.interview.RealTimeMarkToMarket.dao.OtcOptionContract;
import me.interview.RealTimeMarkToMarket.dao.Underlier;

import java.math.BigDecimal;

@Data
public class Contract {
    public enum TYPE {OTC, HDG}

    private String contractNum;
    private TYPE type;
    private Underlier underlier;
    private String product;
    private BigDecimal dailyPnL;
    private BigDecimal delta;
    private BigDecimal gamma;
    private BigDecimal vega;

    public static Contract fromDAO(OtcOptionContract dao) {
        Contract contract = new Contract();
        contract.setType(TYPE.OTC);
        contract.setContractNum(dao.getContractNum());
        contract.setUnderlier(dao.getUnderlier());
        contract.setProduct(dao.getOptionProduct());
        return contract;
    }

    public static Contract fromDAO(HedgePosition dao) {
        Contract contract = new Contract();
        contract.setType(TYPE.HDG);
        contract.setContractNum(dao.getHedgeNum());
        contract.setUnderlier(dao.getUnderlier());
        contract.setProduct("场内期货");
        return contract;
    }
}
