package com.SmartSaudiStockAdvisor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@Slf4j
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SmartSaudiStockAdvisorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartSaudiStockAdvisorApplication.class, args);
		log.info("============ Success ============");
		System.out.println("============ Success ============");
	}

}
