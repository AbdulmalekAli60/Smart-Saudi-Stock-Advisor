package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.LogInDTO;
import com.SmartSaudiStockAdvisor.dto.SignUpDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import com.SmartSaudiStockAdvisor.entity.User;
import com.SmartSaudiStockAdvisor.exception.AlreadyExistsException;
import com.SmartSaudiStockAdvisor.exception.EntryNotFoundException;
import com.SmartSaudiStockAdvisor.exception.UserRegistrationException;
import com.SmartSaudiStockAdvisor.repo.UserRepo;
import com.SmartSaudiStockAdvisor.service.AuthService;
import com.SmartSaudiStockAdvisor.service.JWTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponseDTO signUp(SignUpDTO signUpDTO) {

        Optional<User> existingUser = userRepo.findByEmailOrUsername(signUpDTO.getEmail(), signUpDTO.getUsername());

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getEmail().equals(signUpDTO.getEmail())) {
                throw new AlreadyExistsException("Email is already used");
            } else {
                throw new AlreadyExistsException("Username is already used");
            }
        }

        User newUser = new User(
                signUpDTO.getName(),
                signUpDTO.getUsername(),
                passwordEncoder.encode(signUpDTO.getPassword()),
                signUpDTO.getEmail()
        );

        try{
            User savedUser = userRepo.save(newUser);
            log.info("User successfully joined with id: {}", savedUser.getUserId());

            return new  UserResponseDTO(savedUser, "User Registered Successfully ");
        }catch (DataAccessException e){
            log.error("Database error during signup for email: {}", signUpDTO.getEmail(), e);
            throw new UserRegistrationException("Failed to create account, please try again");
        }
    }

    @Override
    public UserResponseDTO logIn(LogInDTO logInDTO) {

        Optional<User> loggedInUser = userRepo.findByEmail(logInDTO.getEmail());

        if(loggedInUser.isPresent()){
            User user = loggedInUser.get();
            boolean isPasswordValid = passwordEncoder.matches(logInDTO.getPassword(), user.getPassword());

            if (isPasswordValid) {
                log.info("User logged in successfully. Email: {}", logInDTO.getEmail());
                return new UserResponseDTO(user, "Logged in successfully");
            }
        }

        log.error("User with these credentials dose not exists. Email: {}", logInDTO.getEmail());
        throw new EntryNotFoundException("User with these credentials dose not exists");
    }
}
