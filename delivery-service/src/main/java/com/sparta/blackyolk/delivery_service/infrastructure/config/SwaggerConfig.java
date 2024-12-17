package com.sparta.blackyolk.delivery_service.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Delivery API",
                version = "v1",
                description = "Delivery API 입니다."
        )
)
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT"; // 보안 스키마 이름

        // Security Requirement 생성
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);

        // Security Scheme 생성
        Components components = new Components()
                .addSecuritySchemes(jwt, new SecurityScheme()
                        .name(jwt)
                        .type(SecurityScheme.Type.HTTP) // HTTP 타입 보안 스키마
                        .scheme("bearer")              // Bearer 인증 방식
                        .bearerFormat("JWT")           // JWT 형식 사용
                );

        // OpenAPI 객체 반환
        return new OpenAPI()
                .addServersItem(new Server().url("/")) // 서버 설정
                .addSecurityItem(securityRequirement) // 보안 요구 사항 추가
                .components(components);              // 보안 컴포넌트 추가
    }
}
