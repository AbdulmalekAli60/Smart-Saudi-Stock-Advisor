package com.SmartSaudiStockAdvisor.service;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public interface JWTService {

    String generateToken(String email, String role);

    String extractEmail(String token);

    boolean isTokenValid(String token);

    String extractUserRoleFromToken(String token);

    Claims extractClaims(String token);

}
