package me.interview.RealTimeMarkToMarket.service;

import me.interview.RealTimeMarkToMarket.model.OtcOptionPortfolio;
import me.interview.RealTimeMarkToMarket.repository.OtcOptionPortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OtcOptionPortfolioService {
    @Autowired
    private OtcOptionPortfolioRepository portfolioRepository;

    public List<OtcOptionPortfolio> getOptionPortfolios() {
        return portfolioRepository.findAll();
    }
}
