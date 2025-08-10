package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.LogInDTO;
import com.SmartSaudiStockAdvisor.dto.SignUpDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import com.SmartSaudiStockAdvisor.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<UserResponseDTO> signUp(@RequestBody @Valid SignUpDTO signUpDTO){
        UserResponseDTO response = authService.signUp(signUpDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid  LogInDTO logInDTO){
        UserResponseDTO response = authService.logIn(logInDTO);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
