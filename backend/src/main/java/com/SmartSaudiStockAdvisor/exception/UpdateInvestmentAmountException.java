package com.SmartSaudiStockAdvisor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UpdateInvestmentAmountException extends RuntimeException {
    public UpdateInvestmentAmountException(String message) {
        super(message);
    }
}
