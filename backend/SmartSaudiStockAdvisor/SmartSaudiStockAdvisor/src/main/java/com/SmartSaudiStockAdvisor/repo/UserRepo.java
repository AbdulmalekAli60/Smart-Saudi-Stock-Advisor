package com.SmartSaudiStockAdvisor.repo;

import com.SmartSaudiStockAdvisor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmailOrUsername(String email, String username);
}
