package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.InvestAmountDTO;
import com.SmartSaudiStockAdvisor.entity.User;
import com.SmartSaudiStockAdvisor.exception.UpdateInvestmentAmountException;
import com.SmartSaudiStockAdvisor.exception.UserNotFound;
import com.SmartSaudiStockAdvisor.repo.UserRepo;
import com.SmartSaudiStockAdvisor.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo repo) {
        this.userRepo = repo;
    }

    @Override
    @Transactional
    public String updateInvestAmount(Long userId, InvestAmountDTO investAmountDTO) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> {
            log.info("User was not found to update invest amount. passed Id: {}", userId);
            throw new UserNotFound("User was not found to update invest amount.");
        });

        try {
            user.setInvestAmount(investAmountDTO.getInvestAmount());
            log.info("Invest amount has been updated to {}, for user: {}", investAmountDTO.getInvestAmount(), userId);
            userRepo.save(user);
            return "Invest Amount has been updated Successfully";
        }catch (DataAccessException e){
            log.error("Database error during updating investment amount for user: {}", userId, e);
            throw new UpdateInvestmentAmountException("Failed to update investment amount");
        }
    }
}
