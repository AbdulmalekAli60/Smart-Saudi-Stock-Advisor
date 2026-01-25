package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.dto.LogInDTO;
import com.SmartSaudiStockAdvisor.dto.SignUpDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import com.SmartSaudiStockAdvisor.entity.User;
import com.SmartSaudiStockAdvisor.exception.AlreadyExistsException;
import com.SmartSaudiStockAdvisor.exception.EntryNotFoundException;
import com.SmartSaudiStockAdvisor.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepo mockUserRepo;

    @Mock
    private BCryptPasswordEncoder mockPasswordEncoder;

    @Mock
    private MessageSource mockMessageSource;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp(){
        when(mockMessageSource.getMessage(anyString(), any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName(value = "SignUp should save new user when the user dose not already exists")
    void testSignUp_Success() {
        SignUpDTO signUpDTO = new SignUpDTO("test@gmail.com", "teest!!!", "test1234@", "MyNameIsTest");

        User newUser = new User("teest!!!", "MyNameIsTest", "test1234@", "test@gmail.com", "USER");
        newUser.setUserId(1L);

        when(mockUserRepo.findByEmailOrUsername(signUpDTO.email(), signUpDTO.username()))
                .thenReturn(Optional.empty());

        when(mockPasswordEncoder.encode(signUpDTO.password()))
                .thenReturn("Password hashed");

        when(mockUserRepo.save(any(User.class)))
                .thenReturn(newUser);

        UserResponseDTO responseDTO = authService.signUp(signUpDTO);

        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.userId());
        assertEquals("auth-service.register-successfully.message", responseDTO.message());

        verify(mockUserRepo, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName(value = "SignUp should throw error when email is used")
    void testSignUp_emailUsed() {
        SignUpDTO signUpDTO = new SignUpDTO("exist@gmail.com", "I exist", "exists1234@", "MyNameIsExist");

        User existingUser = new User("Existing!!!", "ty", "test1234@", "exist@gmail.com", "USER");

        when(mockUserRepo.findByEmailOrUsername(signUpDTO.email(), signUpDTO.username()))
                .thenReturn(Optional.of(existingUser));

        AlreadyExistsException existsException  = assertThrows(AlreadyExistsException.class, () -> {
            authService.signUp(signUpDTO);
        });

        assertEquals("auth-service.email.already-used", existsException.getMessage());

        verify(mockUserRepo, never()).save(any(User.class));
    }

    @Test
    @DisplayName(value = "SignUp should throw error when username is used")
    void testSignUp_usernameUsed() {
        SignUpDTO signUpDTO = new SignUpDTO("input@gmail.com", "I exist", "exists1234@", "MyNameIsExist");

        User existingUser = new User("Existing!!!", "MyNameIsExist", "test1234@", "exist@gmail.com", "USER");

        when(mockUserRepo.findByEmailOrUsername(signUpDTO.email(), signUpDTO.username()))
                .thenReturn(Optional.of(existingUser));

        AlreadyExistsException existsException  = assertThrows(AlreadyExistsException.class, () -> {
            authService.signUp(signUpDTO);
        });

        assertEquals("auth-service.username.already-used", existsException.getMessage());

        verify(mockUserRepo, never()).save(any(User.class));
    }


    @Test
    @DisplayName("Log In successful")
    void logIn_success() {
        LogInDTO logInDTO = new LogInDTO("hi@gmail.com", "1234fgy@");

        User existingUser = new User("Here!!!", "Iamfound", "1234fgy@", "hi@gmail.com", "USER");
        existingUser.setUserId(2L);

        when(mockUserRepo.findByEmailIgnoreCase(logInDTO.email()))
                .thenReturn(Optional.of(existingUser));

        when(mockPasswordEncoder.matches("1234fgy@", "1234fgy@"))
                .thenReturn(true);

        UserResponseDTO responseDTO = authService.logIn(logInDTO);

        assertNotNull(responseDTO);
        assertEquals(2L, responseDTO.userId());
        assertEquals("auth-service.log-in.successfully.message", responseDTO.message());
    }

    @Test
    @DisplayName("Log In should throw EntryNotFoundException for invalid password")
    void testLogIn_InvalidPassword() {
        LogInDTO logInDTO = new LogInDTO("test@example.com", "wrong_password");

        User foundUser = new User("Test User", "testuser", "hashed_password", "test@example.com", "USER");

        when(mockUserRepo.findByEmailIgnoreCase("test@example.com"))
                .thenReturn(Optional.of(foundUser));
        when(mockPasswordEncoder.matches("wrong_password", "hashed_password"))
                .thenReturn(false);

        EntryNotFoundException exception = assertThrows(EntryNotFoundException.class, () -> {
            authService.logIn(logInDTO);
        });

        assertEquals("auth-service.log-in.invalid.cred.message", exception.getMessage());
    }


    @Test
    @DisplayName("Log In should throw EntryNotFoundException when user not found")
    void testLogIn_UserNotFound() {
        LogInDTO logInDTO = new LogInDTO("not-found@example.com", "password123");

        when(mockUserRepo.findByEmailIgnoreCase("not-found@example.com"))
                .thenReturn(Optional.empty());

        EntryNotFoundException exception = assertThrows(EntryNotFoundException.class, () -> {
            authService.logIn(logInDTO);
        });

        assertEquals("auth-service.log-in.cred.not-exist.message", exception.getMessage());

        verify(mockPasswordEncoder, never()).matches(anyString(), anyString());
    }
}