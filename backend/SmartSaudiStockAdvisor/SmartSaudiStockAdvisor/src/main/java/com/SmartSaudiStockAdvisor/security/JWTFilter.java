package com.SmartSaudiStockAdvisor.security;

import com.SmartSaudiStockAdvisor.exception.TokenIsNotValidException;
import com.SmartSaudiStockAdvisor.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@Component
public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
//    private final ApplicationContext applicationContext;
    private final CustomUserDetailsService customUserDetailsService;
    @Autowired
    public JWTFilter(JWTService jwtService, ApplicationContext applicationContext, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
//        this.applicationContext = applicationContext;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      try {
          String authHeader =  request.getHeader("Authorization");
          String extractedToken = extractTokenFromHeader(authHeader);

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
      }catch (Exception e){
          throw new TokenIsNotValidException("Failed to Extract token and setting the context");
      }
    }

    private String extractTokenFromHeader(String authHeader){
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        log.error("Failed to Extract token from header");
        return null;
    }
}
