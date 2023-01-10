package me.interview.RealTimeMarkToMarket.repository;

import me.interview.RealTimeMarkToMarket.dao.OtcOptionContract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtcOptionContractRepository extends JpaRepository<OtcOptionContract, String> {
}
