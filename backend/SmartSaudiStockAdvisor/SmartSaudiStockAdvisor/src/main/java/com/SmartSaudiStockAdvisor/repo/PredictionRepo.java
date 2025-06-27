package com.SmartSaudiStockAdvisor.repo;

import com.SmartSaudiStockAdvisor.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionRepo extends JpaRepository<Prediction, Long> {
}
