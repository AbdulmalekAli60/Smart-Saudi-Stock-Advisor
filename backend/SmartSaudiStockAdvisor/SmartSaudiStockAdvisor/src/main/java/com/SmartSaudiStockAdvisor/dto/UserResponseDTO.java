package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.User;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class UserResponseDTO {

    private String message;
    private String name;
    private String username;
    private String email;
    private Timestamp joinDate;
    private Long userId;
    private String role;
    private BigDecimal investAmount;

    public UserResponseDTO() {
    }

    public UserResponseDTO(User user, String message) {
        this.message = message;
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.joinDate = user.getJoinDate();
        this.userId = user.getUserId();
        this.role = user.getRole();
        this.investAmount = user.getInvestAmount();
    }

    public UserResponseDTO(User user) {
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.joinDate = user.getJoinDate();
        this.userId = user.getUserId();
        this.role = user.getRole();
        this.investAmount = user.getInvestAmount();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }
}
