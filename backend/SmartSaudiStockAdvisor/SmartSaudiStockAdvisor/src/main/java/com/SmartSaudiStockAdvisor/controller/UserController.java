package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.InvestAmountDTO;
import com.SmartSaudiStockAdvisor.dto.UpdateAccountDetailsDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import com.SmartSaudiStockAdvisor.service.ETagService;
import com.SmartSaudiStockAdvisor.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, ETagService eTagService) {
        this.userService = userService;
    }

    @PatchMapping(value = "investment-amount/{id}")
    public ResponseEntity<String> updateInvestAmount(@PathVariable(name = "id") Long userId, @Valid @RequestBody InvestAmountDTO investAmountDTO){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateInvestAmount(userId, investAmountDTO));
    }

    @PatchMapping(value = "/update")
    public ResponseEntity<UserResponseDTO> updateAccountInfo(@Valid @RequestBody UpdateAccountDetailsDTO updateAccountDetailsDTO){
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.updateUserInformation(updateAccountDetailsDTO));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteUserAccount(){
        ResponseCookie cookie = ResponseCookie.from("JWT-TOKEN", "")
                .httpOnly(true)
                .secure(false) // change it in prod
                .sameSite("Strict")
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(userService.deleteAccount());
    }

    @GetMapping(value = "/personal")
    public ResponseEntity<UserResponseDTO> currentUser(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.freshUserInfo());
    }
}
