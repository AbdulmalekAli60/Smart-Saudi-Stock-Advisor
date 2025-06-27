package com.SmartSaudiStockAdvisor.repo;

import com.SmartSaudiStockAdvisor.entity.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchListRepo extends JpaRepository<WatchList, Long> {
}
