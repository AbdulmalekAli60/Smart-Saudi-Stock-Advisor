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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String token = jwtService.generateToken(responseDTO.role(), responseDTO.role());

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.SET_COOKIE, constructCookie(token))
                .header("x-Access-Token", token)
                .body(responseDTO);
    }

    @PostMapping(value = "/log-in")
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid LogInDTO logInDTO){
        UserResponseDTO responseDTO = authService.logIn(logInDTO);

        String token = jwtService.generateToken(responseDTO.email(), responseDTO.role());

        return  ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, constructCookie(token))
                .header("x-Access-Token", token)
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
    public ResponseEntity<HttpHeaders> refreshToken(@RequestBody Map<String, String> body) {
        String token = null;

        if(body != null && body.containsKey("token")){
            token = body.get("token");
        }

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!jwtService.isTokenValidButExpired(token)) {
            log.warn("Token is still valid but expired");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtService.extractEmail(token);
        String userRole = jwtService.extractUserRoleFromToken(token);

        String newToken = jwtService.generateToken(email, userRole);

        String cookie = constructCookie(newToken);
        log.info("======= token refreshed =======");
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie)
                .header("x-Access-Token", cookie)
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
