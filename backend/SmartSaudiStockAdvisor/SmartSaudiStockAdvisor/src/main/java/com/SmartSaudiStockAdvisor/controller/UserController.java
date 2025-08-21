package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.InvestAmountDTO;
import com.SmartSaudiStockAdvisor.service.ETagService;
import com.SmartSaudiStockAdvisor.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final ETagService eTagService;

    @Autowired
    public UserController(UserService userService, ETagService eTagService) {
        this.userService = userService;
        this.eTagService = eTagService;
    }

    @PatchMapping(value = "investment-amount/{id}")
    public ResponseEntity<String> updateInvestAmount(@PathVariable(name = "id") Long userId, @Valid @RequestBody InvestAmountDTO investAmountDTO){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateInvestAmount(userId, investAmountDTO));
    }
}
