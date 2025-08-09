package com.SmartSaudiStockAdvisor.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//mark this class as the exception handler class
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {UserNotFound.class, UserAlreadyExists.class})
    public ResponseEntity<ErrorResponse> handleExceptions(RuntimeException ex){
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
