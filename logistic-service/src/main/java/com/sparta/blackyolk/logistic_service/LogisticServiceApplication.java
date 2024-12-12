package com.sparta.blackyolk.logistic_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LogisticServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogisticServiceApplication.class, args);
	}

}
