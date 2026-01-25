package com.SmartSaudiStockAdvisor.repo;

import com.SmartSaudiStockAdvisor.entity.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchListRepo extends JpaRepository<WatchList, Long> {
    // must be both user id and company id to be unique
    boolean existsByUserUserIdAndCompanyCompanyId(Long userId, Long companyId);

    List<WatchList> findAllByUserUserId(Long userId);
}
