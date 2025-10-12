package com.SmartSaudiStockAdvisor.service.ServiceImpl;

import com.SmartSaudiStockAdvisor.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
    public String generateToken(String email, String role) {
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + expiration);
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(email)
                .issuedAt(currentDate)
                .expiration(expirationDate)
                .claim("role", role)
                .signWith(getSignKey())
                .compact();
    }

    @Override
    public String extractEmail(String token) {
        try{
            Claims claims = extractClaims(token);

            return claims != null ? claims.getSubject() : null;

        }catch (Exception e){
            log.error("Error extracting user ID from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = extractClaims(token); // if we get claims then the data in token is valid, before checking date
            if (claims == null) {
                return true;
            }

            Date expiration = claims.getExpiration();
            return expiration != null && expiration.before(new Date());

        } catch (ExpiredJwtException e) {
            return true;

        } catch (Exception e) {
            log.error("Error while refreshing the token: {}", e.getMessage());
            return true;
        }
    }

    @Override
    public boolean isTokenValidButExpired(String token) {
        Claims claims = extractClaims(token);
        return claims != null && isTokenExpired(token);
    }

    @Override
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (ExpiredJwtException e){
            log.debug("Token is expired but its data is valid");
            return e.getClaims();

        } catch (Exception e){
            log.error("Error extracting claims from token: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public String extractUserRoleFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.get("role", String.class);
    }

    private SecretKey getSignKey() {
        byte[] keyInBytes = Decoders.BASE64.decode(secrete);
        return Keys.hmacShaKeyFor(keyInBytes);
    }
}
