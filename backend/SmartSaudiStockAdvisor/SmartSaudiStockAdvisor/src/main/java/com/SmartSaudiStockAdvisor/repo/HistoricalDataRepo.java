package com.SmartSaudiStockAdvisor.repo;

import com.SmartSaudiStockAdvisor.entity.HistoricalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricalDataRepo extends JpaRepository<HistoricalData, Long> {

//    @Query(value = "from HistoricalData where company.companyId= :id order by dataDate Asc")
    List<HistoricalData> findHistoricalDataByCompanyCompanyIdOrderByDataDateAsc(Long companyId);
}
