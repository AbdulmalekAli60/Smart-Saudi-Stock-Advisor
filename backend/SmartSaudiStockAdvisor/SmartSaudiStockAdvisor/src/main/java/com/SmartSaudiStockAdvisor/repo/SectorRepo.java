package com.SmartSaudiStockAdvisor.repo;

import com.SmartSaudiStockAdvisor.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorRepo extends JpaRepository<Sector, Long> {
}
