package com.SmartSaudiStockAdvisor.repo;

import com.SmartSaudiStockAdvisor.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepo extends JpaRepository<Prediction, Long> {

    List<Prediction> findByCompanyCompanyIdOrderByPredictionDateAsc(Long companyId);

    Prediction findTop1ByCompanyCompanyIdOrderByPredictionDateDesc(Long companyId);
}
