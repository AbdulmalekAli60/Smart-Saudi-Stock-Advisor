package com.SmartSaudiStockAdvisor.controller;

import com.SmartSaudiStockAdvisor.dto.LogInDTO;
import com.SmartSaudiStockAdvisor.dto.SignUpDTO;
import com.SmartSaudiStockAdvisor.dto.UserResponseDTO;
import com.SmartSaudiStockAdvisor.service.AuthService;
import com.SmartSaudiStockAdvisor.service.JWTService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTService jwtService;
    private final MessageSource messageSource;

    @Value(value = "${app.JWT_EXPIRATION_TIME}")
    private Long expirationDuration;

    @Autowired
    public AuthController(AuthService authService, JWTService jwtService, MessageSource source) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.messageSource = source;
    }

    @PostMapping(value = "/sign-up")
    public ResponseEntity<UserResponseDTO> signUp(@RequestBody @Valid SignUpDTO signUpDTO){
        UserResponseDTO responseDTO = authService.signUp(signUpDTO);
        String token = jwtService.generateToken(responseDTO.getEmail(), responseDTO.getRole());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, constructCookie(token))
                .body(responseDTO);
    }

    @PostMapping(value = "/log-in")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid  LogInDTO logInDTO){
        UserResponseDTO responseDTO = authService.logIn(logInDTO);

        String token = jwtService.generateToken(responseDTO.getEmail(), responseDTO.getRole());

        return  ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, constructCookie(token))
                .body(responseDTO);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("JWT-TOKEN", "")
                .httpOnly(true)
                .secure(false) // change it in prod
                .sameSite("Strict")
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", getMessage("auth-controller-logout.message", null)));
    }

    @PostMapping(value = "/refresh-token")
    public ResponseEntity<HttpHeaders> refreshToken(
            @CookieValue(name = "JWT-TOKEN", required = false) String cookieValue) {

        log.info("====== REFRESH ENDPOINT CALLED ======");
        log.info("=== Cookie value: {}", cookieValue);

        if (cookieValue == null) {
            log.warn("No token provided");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        if (!jwtService.isTokenValidButExpired(cookieValue)) {
            log.warn("Token is still valid but expired or has invalid sig");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtService.extractEmail(cookieValue);
        String userRole = jwtService.extractUserRoleFromToken(cookieValue);

        log.info("Refreshing token for user: {}", email);

        String newToken = jwtService.generateToken(email, userRole);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, constructCookie(newToken))
                .build();
    }

    private String constructCookie(String token){
        ResponseCookie cookie = ResponseCookie.from("JWT-TOKEN", token)
                .httpOnly(true)
                .secure(false) // change it in prod
                .sameSite("Strict")
                .maxAge(Duration.ofMillis(expirationDuration))
                .path("/")
                .build();

        return cookie.toString();
    }

    private String getMessage(String key, Object[] params){
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }
}
