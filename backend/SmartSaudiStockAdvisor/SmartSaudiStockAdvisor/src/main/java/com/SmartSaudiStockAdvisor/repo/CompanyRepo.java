package com.SmartSaudiStockAdvisor.repo;

import com.SmartSaudiStockAdvisor.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {
}
