package com.SmartSaudiStockAdvisor.dto;

import jakarta.validation.constraints.Email;

import java.sql.Timestamp;

public class CreateUserDTO {

    private Long user_id;
    private String email;
    private float investAmount;
    private Timestamp joinDate;
    private String name;
    private String password;
    private String username;

    public CreateUserDTO() {
    }

    public CreateUserDTO(Long user_id, String email, float investAmount, Timestamp joinDate, String name, String password, String username) {
        this.user_id = user_id;
        this.email = email;
        this.investAmount = investAmount;
        this.joinDate = joinDate;
        this.name = name;
        this.password = password;
        this.username = username;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(float investAmount) {
        this.investAmount = investAmount;
    }

    public Timestamp getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Timestamp joinDate) {
        this.joinDate = joinDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
