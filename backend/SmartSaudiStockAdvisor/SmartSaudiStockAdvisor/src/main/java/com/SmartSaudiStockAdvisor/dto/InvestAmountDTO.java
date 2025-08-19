package com.SmartSaudiStockAdvisor.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class InvestAmountDTO {

    @NotNull(message = "Investment amount is required")
    @DecimalMax(value = "99999999.99", message = "Investment amount cannot exceed 99,999,999.99")
    @DecimalMin(value = "0", message = "Minimum investment amount is 0")
    private BigDecimal investAmount;

    public InvestAmountDTO() {
    }

    public InvestAmountDTO(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }
}
