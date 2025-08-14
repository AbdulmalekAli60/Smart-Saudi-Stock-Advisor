package com.SmartSaudiStockAdvisor.service;

import com.SmartSaudiStockAdvisor.dto.InvestAmountDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    String updateInvestAmount(Long userId,InvestAmountDTO investAmountDTO);
}
