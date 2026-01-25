package com.SmartSaudiStockAdvisor.security;

import com.SmartSaudiStockAdvisor.service.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public JWTFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        boolean shouldSkip = path.startsWith("/auth/");
        log.info("shouldNotFilter check: Path: {}, Skip filter: {}", path, shouldSkip);
        return shouldSkip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.startsWith("/auth/")) {
            log.info("Skipping JWT filter for auth path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String extractedToken = extractTokenFromCookie(request.getCookies());

            if (extractedToken == null) {
                log.warn("No token found in cookies for path: {}", request.getServletPath());
                filterChain.doFilter(request, response);
                return;
            }

            boolean isTokenExpired = jwtService.isTokenExpired(extractedToken);
            if (isTokenExpired) {
                log.warn("Token is expired for path: {}", request.getServletPath());
                filterChain.doFilter(request, response);
                return;
            }

            String userEmail = jwtService.extractEmail(extractedToken);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: ", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired");
        } catch (JwtException e) {
            log.error("JWT token is invalid: ", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
        } catch (UsernameNotFoundException e) {
            log.error("User not found during JWT authentication: ", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not found");
        }
    }

    private String extractTokenFromCookie(Cookie[] cookies){
        if(cookies != null){
            for (Cookie cookie : cookies){
                if("JWT-TOKEN".equals(cookie.getName())){
                    log.info("Token found in cookie");
                    return cookie.getValue();
                }
            }
        }
        log.debug("Failed to Extract token from cookie");
        return null;
    }
}
