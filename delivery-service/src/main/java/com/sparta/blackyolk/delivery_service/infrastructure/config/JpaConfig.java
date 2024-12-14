package com.sparta.blackyolk.delivery_service.infrastructure.config;

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
        // 고정 UUID 반환
        return () -> Optional.of(UUID.fromString("2b6119b0-69bd-4749-8505-dadb171a5b1d"));
    }
}