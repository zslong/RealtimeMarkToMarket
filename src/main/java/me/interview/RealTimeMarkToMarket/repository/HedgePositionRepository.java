package me.interview.RealTimeMarkToMarket.repository;

import me.interview.RealTimeMarkToMarket.dao.HedgePosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HedgePositionRepository extends JpaRepository<HedgePosition, String> {
}
