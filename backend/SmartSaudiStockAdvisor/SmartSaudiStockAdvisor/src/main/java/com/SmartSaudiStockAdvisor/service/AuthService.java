package com.SmartSaudiStockAdvisor.service;

import com.SmartSaudiStockAdvisor.dto.LogInDTO;
import com.SmartSaudiStockAdvisor.dto.SignUpDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    UserResponseDTO signUp(SignUpDTO signUpDTO);

    UserResponseDTO logIn(LogInDTO logInDTO);
}
