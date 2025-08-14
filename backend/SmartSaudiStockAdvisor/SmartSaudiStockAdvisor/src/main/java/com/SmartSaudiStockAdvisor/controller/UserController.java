package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.InvestAmountDTO;
import com.SmartSaudiStockAdvisor.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping(value = "investment-amount/{id}")
    public ResponseEntity<String> updateInvestAmount(@PathVariable(name = "id") Long userId, @Valid @RequestBody InvestAmountDTO investAmountDTO){
        String message = userService.updateInvestAmount(userId, investAmountDTO);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
