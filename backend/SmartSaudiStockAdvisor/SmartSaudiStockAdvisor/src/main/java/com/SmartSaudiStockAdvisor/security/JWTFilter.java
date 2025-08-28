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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      try {

          if (shouldNotFilter(request)) {
              filterChain.doFilter(request, response);
              return;
          }

          String extractedToken = extractTokenFromCookie(request.getCookies());

          if(extractedToken == null){
              filterChain.doFilter(request, response);
              return;
          }

          boolean isTokenValid = jwtService.isTokenValid(extractedToken);

          if(!isTokenValid){
              filterChain.doFilter(request, response);
                return;
          }

          String userEmail = jwtService.extractEmail(extractedToken);

          if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){

              UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

              UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                      userDetails, null, userDetails.getAuthorities()
              );

              authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
              SecurityContextHolder.getContext().setAuthentication(authenticationToken);
              filterChain.doFilter(request, response);
              return;
          }

          filterChain.doFilter(request, response);

      }catch (ExpiredJwtException e) {
        log.error("JWT token is expired: ", e);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      } catch (JwtException e) {
        log.error("JWT token is invalid: ", e);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }catch (UsernameNotFoundException e) {
          log.error("User not found during JWT authentication: ", e);
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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
