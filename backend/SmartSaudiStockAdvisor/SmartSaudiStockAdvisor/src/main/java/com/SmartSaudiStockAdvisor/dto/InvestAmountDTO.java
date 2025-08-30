package com.SmartSaudiStockAdvisor.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class InvestAmountDTO {

    @NotNull(message = "{validation.not-null.invest-amount}")
    @DecimalMax(value = "99999999.99", message = "{validation.max-decimal.invest-amount}")
    @DecimalMin(value = "0", message = "{validation.min-decimal.invest-amount}")
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
