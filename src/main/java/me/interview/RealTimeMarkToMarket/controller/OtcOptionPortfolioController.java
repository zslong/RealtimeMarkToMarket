package me.interview.RealTimeMarkToMarket.controller;

import me.interview.RealTimeMarkToMarket.model.OtcOptionPortfolio;
import me.interview.RealTimeMarkToMarket.service.OtcOptionPortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OtcOptionPortfolioController {
    @Autowired
    private OtcOptionPortfolioService portfolioService;

    @GetMapping("/portfolios")
    public List<OtcOptionPortfolio> getPortfolios() {
        return  portfolioService.getOptionPortfolios();
    }
}
