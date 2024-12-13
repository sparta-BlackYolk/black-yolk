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
        return () -> Optional.of(UUID.fromString("b067c637-2c80-41fb-9f2e-8b671b5abfd0"));
    }
}