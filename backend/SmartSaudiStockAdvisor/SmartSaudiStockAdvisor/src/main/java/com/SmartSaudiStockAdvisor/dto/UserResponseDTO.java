package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.User;
import java.math.BigDecimal;
import java.sql.Timestamp;

public record UserResponseDTO(
        String message,
        String name,
        String username,
        String email,
        Timestamp joinDate,
        Long userId,
        String role,
        BigDecimal investAmount
) {

    public UserResponseDTO(User user, String message) {
        this(
                message,
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getJoinDate(),
                user.getUserId(),
                user.getRole(),
                user.getInvestAmount()
        );
    }


    public UserResponseDTO(User user) {
        this(user, null);
    }
}