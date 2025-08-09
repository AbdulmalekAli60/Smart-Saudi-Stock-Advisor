package com.SmartSaudiStockAdvisor.dto;

import com.SmartSaudiStockAdvisor.entity.User;

import java.sql.Timestamp;

public class UserResponseDTO {

    private String name;
    private String username;
    private String email;
    private Timestamp joinDate;

    public UserResponseDTO(User newUser) {
        this.name = newUser.getName();
        this.username = newUser.getUsername();
        this.email = newUser.getEmail();
        this.joinDate = newUser.getJoinDate();
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
}
