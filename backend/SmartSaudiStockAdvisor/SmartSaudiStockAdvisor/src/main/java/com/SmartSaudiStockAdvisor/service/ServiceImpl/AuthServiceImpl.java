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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Autowired
    public AuthServiceImpl(UserRepo userRepo, BCryptPasswordEncoder passwordEncoder,MessageSource source) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.messageSource = source;
    }

    @Override
    @Transactional
    public UserResponseDTO signUp(SignUpDTO signUpDTO) {

        Optional<User> existingUser = userRepo.findByEmailOrUsername(signUpDTO.getEmail(), signUpDTO.getUsername());

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (user.getEmail().equals(signUpDTO.getEmail())) {
                throw new AlreadyExistsException(getMessage("auth-service.email.already-used"));
            } else {
                throw new AlreadyExistsException(getMessage("auth-service.username.already-used"));
            }
        }

        User newUser = new User(
                signUpDTO.getName().trim(),
                signUpDTO.getUsername().trim(),
                passwordEncoder.encode(signUpDTO.getPassword()),
                signUpDTO.getEmail().trim(),
                "USER" // by default all have the role user
        );

        try{
            User savedUser = userRepo.save(newUser);
            log.info("User successfully joined with id: {}", savedUser.getUserId());

            return new  UserResponseDTO(savedUser, getMessage("auth-service.register-successfully.message"));
        }catch (DataAccessException e){
            log.error("Database error during signup for email: {}", signUpDTO.getEmail(), e);
            throw new UserRegistrationException(getMessage("auth-service.register-failed.message"));
        }
    }

    @Override
    public UserResponseDTO logIn(LogInDTO logInDTO) {
        String email = logInDTO.getEmail().trim();

        Optional<User> loggedInUser = userRepo.findByEmailIgnoreCase(email);

        if (loggedInUser.isPresent()) {
            User user = loggedInUser.get();
            log.info("User found with email: '{}'", user.getEmail());

            boolean isPasswordValid = passwordEncoder.matches(logInDTO.getPassword(), user.getPassword());

            if (isPasswordValid) {
                log.info("User logged in successfully. Email: {}", email);
                return new UserResponseDTO(user, getMessage("auth-service.log-in.successfully.message"));
            } else {
                log.warn("Invalid password for email: '{}'", email);
                throw new EntryNotFoundException(getMessage("auth-service.log-in.invalid.cred.message"));
            }
        } else {
            log.error("No user found with email: '{}'", email);
            throw new EntryNotFoundException(getMessage("auth-service.log-in.cred.not-exist.message"));
        }
    }

    private String getMessage(String key){
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}
