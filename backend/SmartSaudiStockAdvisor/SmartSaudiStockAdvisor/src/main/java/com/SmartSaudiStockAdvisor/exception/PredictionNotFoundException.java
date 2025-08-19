package com.SmartSaudiStockAdvisor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PredictionNotFoundException extends RuntimeException{
    public PredictionNotFoundException(String message, Long companyId) {
        super(message + companyId);
    }
}
