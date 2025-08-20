package com.SmartSaudiStockAdvisor.service;

import com.SmartSaudiStockAdvisor.dto.InvestAmountDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    String updateInvestAmount(Long userId,InvestAmountDTO investAmountDTO);

    List<UserResponseDTO> getAllUsers();

    String deleteUser(Long userId);
}
