package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.SignUpDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import com.SmartSaudiStockAdvisor.entity.User;
import com.SmartSaudiStockAdvisor.exception.UserAlreadyExists;
import com.SmartSaudiStockAdvisor.repo.UserRepo;
import com.SmartSaudiStockAdvisor.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(final UserRepo userRepo, final BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO signUp(SignUpDTO signUpDTO) {

        if (userRepo.existsByEmail(signUpDTO.getEmail())){
            log.warn("Email is Already used, email: {}", signUpDTO.getEmail());
            throw new UserAlreadyExists("Email is Already used");
        }

        if (userRepo.existsByUsername(signUpDTO.getUsername())){
            log.warn("Username is Already used, username: {}", signUpDTO.getUsername());
            throw new UserAlreadyExists("Username is Already used");
        }

        User newUser = new User(
                signUpDTO.getName(),
                signUpDTO.getUsername(),
                passwordEncoder.encode(signUpDTO.getPassword()),
                signUpDTO.getEmail()
        );

        User savedUser = userRepo.save(newUser);
        log.info("User successfully joined with id: {}", savedUser.getUserId());

        return new  UserResponseDTO(savedUser);
    }
}
