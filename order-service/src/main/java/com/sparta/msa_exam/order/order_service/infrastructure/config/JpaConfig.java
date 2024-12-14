package com.sparta.msa_exam.order.order_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@Configuration
public class JpaConfig {
    @Bean
    public AuditorAware<UUID> auditorAware() {
        return () -> Optional.of(UUID.fromString("636d6964-dd9d-41c8-a6e7-b8837ec5be73"));
    }
}