package com.SmartSaudiStockAdvisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SmartSaudiStockAdvisorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartSaudiStockAdvisorApplication.class, args);
		log.info("============ Success ============");
		System.out.println("============ Success ============");
	}
}
