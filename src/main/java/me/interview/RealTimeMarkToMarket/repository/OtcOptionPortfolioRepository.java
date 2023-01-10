package me.interview.RealTimeMarkToMarket.repository;

import me.interview.RealTimeMarkToMarket.dao.OtcOptionPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtcOptionPortfolioRepository extends JpaRepository<OtcOptionPortfolio, Long> {
}
