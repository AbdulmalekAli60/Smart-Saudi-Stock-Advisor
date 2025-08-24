package com.SmartSaudiStockAdvisor.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public interface JWTService {

    String generateToken(String email);

    String extractEmail(String token);

    boolean isTokenValid(String token);

//    boolean isTokenExpired(String token);

    Claims extractClaims(String token);

}
