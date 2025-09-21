package com.SmartSaudiStockAdvisor.service;

import com.SmartSaudiStockAdvisor.dto.InvestAmountDTO;
import com.SmartSaudiStockAdvisor.dto.UpdateAccountDetailsDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    String updateInvestAmount(Long userId,InvestAmountDTO investAmountDTO);

    UserResponseDTO freshUserInfo();

    @PreAuthorize(value = "hasRole('ADMIN')")
    List<UserResponseDTO> getAllUsers();

    @PreAuthorize(value = "hasRole('ADMIN')")
    String deleteUser(Long userId);

    UserResponseDTO updateUserInformation(UpdateAccountDetailsDTO updateAccountDetailsDTO);

    @PreAuthorize(value = "hasRole('USER') and !hasRole('ADMIN')")
    String deleteAccount();
}
