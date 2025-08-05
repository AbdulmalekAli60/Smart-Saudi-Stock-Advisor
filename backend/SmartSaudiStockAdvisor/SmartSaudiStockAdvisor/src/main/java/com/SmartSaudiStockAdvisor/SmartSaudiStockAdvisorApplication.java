package com.SmartSaudiStockAdvisor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SmartSaudiStockAdvisorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartSaudiStockAdvisorApplication.class, args);
		log.info("Hi form lombok sl4j");
		System.out.println("============ Success ============");
	}

}
