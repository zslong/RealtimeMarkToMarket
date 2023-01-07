package me.interview.RealTimeMarkToMarket.repository;

import me.interview.RealTimeMarkToMarket.model.OtcOptionPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtcOptionPortfolioRepository extends JpaRepository<OtcOptionPortfolio, Long> {
}
