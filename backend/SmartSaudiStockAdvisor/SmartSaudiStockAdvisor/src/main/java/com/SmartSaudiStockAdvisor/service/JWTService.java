package com.SmartSaudiStockAdvisor.service;

import io.jsonwebtoken.Claims;

public interface JWTService {

    String generateToken(String userId);

    String extractUserId(String token);

    boolean isTokenValid(String token);

    boolean isTokenExpired(String token);

    Claims extractClaims(String token);

}
