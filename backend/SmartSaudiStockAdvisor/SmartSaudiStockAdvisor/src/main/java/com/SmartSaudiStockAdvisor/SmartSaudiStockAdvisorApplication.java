package com.SmartSaudiStockAdvisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmartSaudiStockAdvisorApplication {

	private static Logger log = LoggerFactory.getLogger(SmartSaudiStockAdvisorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SmartSaudiStockAdvisorApplication.class, args);
		log.info("Hello World");
		System.out.println("============ Success ============");
	}

}
