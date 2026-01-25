package com.SmartSaudiStockAdvisor.exception;


import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

//mark this class as the exception handler class
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {EntryNotFoundException.class, AlreadyExistsException.class, UpdateInvestmentAmountException.class, UserRegistrationException.class, PredictionNotFoundException.class, OperationFailedException.class})
    public ResponseEntity<ErrorResponse> handleExceptions(RuntimeException ex){

        HttpStatus httpStatus = ex.getClass().getAnnotation(ResponseStatus.class).value();
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), httpStatus.toString());

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errorMap = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex){
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex){
        ErrorResponse errorResponse = new ErrorResponse("Authorization Problem to access resource", HttpStatus.FORBIDDEN.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
