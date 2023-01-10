package me.interview.RealTimeMarkToMarket.service;

import me.interview.RealTimeMarkToMarket.dto.Contract;
import me.interview.RealTimeMarkToMarket.dto.PricerResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Service
public class PricerService {
    public PricerResult[] pricer(Contract[] contracts) {
        Random random = new Random();
        PricerResult[] res = new PricerResult[contracts.length];
        for (int i = 0; i < contracts.length; i++) {
            Contract c = contracts[i];
            PricerResult pr = new PricerResult();
            pr.setDailyPnL(BigDecimal.valueOf(random.nextDouble() * 1000.00).setScale(2, RoundingMode.HALF_UP));
            pr.setDelta(BigDecimal.valueOf(random.nextInt(5)/5.00).setScale(2, RoundingMode.HALF_UP));
            pr.setGamma(BigDecimal.valueOf(random.nextDouble() * 10.00).setScale(2, RoundingMode.HALF_UP));
            pr.setVega(BigDecimal.valueOf(random.nextDouble() * 100.00).setScale(2, RoundingMode.HALF_UP));
            res[i] = pr;
        }

        return res;
    }
}
