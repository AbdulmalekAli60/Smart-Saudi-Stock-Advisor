package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
public class JWTServiceImpl implements JWTService {

    @Value(value = "${app.JWT_SECRETE}")
    private String secrete;

    @Value(value = "${app.JWT_EXPIRATION_TIME}")
    private Long expiration;

    @Override
    public String generateToken(String userId) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + expiration);
        return Jwts.builder()
                .subject(userId)
                .issuedAt(currentDate)
                .expiration(expirationDate)
//                .claim() here will add the role
                .signWith(getSignKey())
                .compact();
    }

    @Override
    public String extractUserId(String token) {
        try{
            Claims claims = extractClaims(token);

            return claims != null ? claims.getSubject() : null;

        }catch (Exception e){
            log.error("Error extracting user ID from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            if (claims == null) {
                return false;
            }

            Date expiration = claims.getExpiration();
            return expiration != null && !expiration.before(new Date());

        } catch (Exception e) {
            log.error("Error while validating the token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractClaims(token);
            if (claims == null) {
                return true;
            }
            Date tokenExpirationDate = claims.getExpiration();
            return tokenExpirationDate == null || tokenExpirationDate.before(new Date()); // if expiration date is before current, token is expired, it will return true
        }catch (Exception e){
            log.error("Error while checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    @Override
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (Exception e){
            log.error("Error extracting claims from token: {}", e.getMessage());
            return null;
        }
    }

    private SecretKey getSignKey() {
        byte[] keyInBytes = Decoders.BASE64.decode(secrete);
        return Keys.hmacShaKeyFor(keyInBytes);
    }
}
