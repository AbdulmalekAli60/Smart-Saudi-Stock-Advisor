package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.LogInDTO;
import com.SmartSaudiStockAdvisor.dto.SignUpDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import com.SmartSaudiStockAdvisor.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<UserResponseDTO> signUp(@RequestBody @Valid SignUpDTO signUpDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUp(signUpDTO));
    }

    @PostMapping(value = "/log-in")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid  LogInDTO logInDTO){
        return  ResponseEntity.status(HttpStatus.OK).body(authService.logIn(logInDTO));
    }
}
