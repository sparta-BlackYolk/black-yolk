package com.sparta.blackyolk.logistic_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
@PropertySource("classpath:env.properties")
public class LogisticServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogisticServiceApplication.class, args);
	}

}
