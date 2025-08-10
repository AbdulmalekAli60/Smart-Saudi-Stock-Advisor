package com.SmartSaudiStockAdvisor.exception;

import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

public class ErrorResponse {
    private  String errorMessage;
//    private HttpStatus statusCode;
    private String statusCode;
    private Timestamp time;

    public ErrorResponse(String errorMessage, String statusCode) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
        this.time = new Timestamp(System.currentTimeMillis());
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
