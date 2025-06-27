package com.SmartSaudiStockAdvisor.repo;

import com.SmartSaudiStockAdvisor.entity.HistoricalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricalDataRepo extends JpaRepository<HistoricalData, Long> {
}
