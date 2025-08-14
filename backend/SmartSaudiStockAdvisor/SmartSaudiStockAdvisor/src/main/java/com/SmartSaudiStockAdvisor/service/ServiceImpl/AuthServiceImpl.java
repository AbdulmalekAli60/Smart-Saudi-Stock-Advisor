package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.LogInDTO;
import com.SmartSaudiStockAdvisor.dto.SignUpDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import com.SmartSaudiStockAdvisor.entity.User;
import com.SmartSaudiStockAdvisor.exception.UserAlreadyExists;
import com.SmartSaudiStockAdvisor.exception.UserNotFound;
import com.SmartSaudiStockAdvisor.repo.UserRepo;
import com.SmartSaudiStockAdvisor.service.AuthService;
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
    public AuthServiceImpl(final UserRepo userRepo, final BCryptPasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserResponseDTO signUp(SignUpDTO signUpDTO) {

        if (userRepo.existsByEmailOrUsername(signUpDTO.getEmail(), signUpDTO.getUsername())){

            if(userRepo.existsByEmail(signUpDTO.getEmail())){
            log.warn("Email is Already used, email: {}", signUpDTO.getEmail());
            throw new UserAlreadyExists("Email is Already used");
            }

            if (userRepo.existsByUsername(signUpDTO.getUsername())){
                log.warn("Username is Already used, username: {}", signUpDTO.getUsername());
                throw new UserAlreadyExists("Username is Already used");
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
            throw new RuntimeException("Failed to create an account, please try again");
        }

    }

    @Override
    public UserResponseDTO logIn(LogInDTO logInDTO) {

        Optional<User> loggedInUser = userRepo.findByEmail(logInDTO.getEmail());

        if(loggedInUser.isPresent()){
            User user = loggedInUser.get();
            boolean isPasswordValid = passwordEncoder.matches(logInDTO.getPassword(), loggedInUser.get().getPassword());

            if (isPasswordValid) {
                log.info("User logged in successfully. Email: {}", logInDTO.getEmail());
                return new UserResponseDTO(user, "Logged in successfully");
            }
        }

        log.error("User with these credentials dose not exists. Email: {}", logInDTO.getEmail());
        throw new UserNotFound("User with these credentials dose not exists");
    }
}
