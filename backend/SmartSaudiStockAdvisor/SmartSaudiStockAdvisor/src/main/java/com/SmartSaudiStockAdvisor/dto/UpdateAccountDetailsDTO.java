package com.SmartSaudiStockAdvisor.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class UpdateAccountDetailsDTO {

    @Size(min = 3, max = 24, message = "{validation.update-account.name.size}")
    @Pattern(
            regexp = "^[a-zA-Z\\s'\\u0600-\\u06FF-]+$",
            message = "{validation.update-account.name.pattern}"
    )
    private String name;

    @Size(min = 3, max = 24, message = "{validation.update-account.username.size}")
    @Pattern(
            regexp = "^[a-zA-Z0-9_-]+$",
            message = "{validation.update-account.username.pattern}"
    )
    private String username;

    @Size(min = 8, max = 16, message = "{validation.update-account.password.size}")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "{validation.update-account.password.pattern}"
    )
    private String password;

    @Email(message = "{validation.update-account.email.email}")
    private String email;

    @DecimalMax(value = "99999999.99", message = "{validation.max-decimal.invest-amount}")
    @DecimalMin(value = "0", message = "{validation.min-decimal.invest-amount}")
    private BigDecimal investAmount;

    public UpdateAccountDetailsDTO() {
    }

    public UpdateAccountDetailsDTO(String name, String username, String password, String email, BigDecimal amount) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.investAmount = amount;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }
}
