package com.SmartSaudiStockAdvisor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Slf4j
@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        log.info("Bcrypt encoder has been created");
        return new BCryptPasswordEncoder(12);
    }
}
