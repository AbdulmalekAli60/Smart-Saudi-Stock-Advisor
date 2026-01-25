package com.SmartSaudiStockAdvisor.repo;

import com.SmartSaudiStockAdvisor.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {
    boolean existsByTickerName(String tickerName);

    @Query(value = "SELECT c from Company c Where " +
            "LOWER(c.companyArabicName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.companyEnglishName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.tickerName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Company> searchCompanies(@Param(value = "keyword") String keyword);
}
