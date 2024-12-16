package com.sparta.blackyolk.auth_service.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                // Authorization 헤더에서 JWT 토큰 가져오기
                String accessToken = request.getHeader("Authorization");
                if (accessToken != null && accessToken.startsWith("Bearer ")) {
                    // 정확한 형식으로 헤더 추가
                    requestTemplate.header("Authorization", accessToken.trim());
                }
            }
        };
    }
}
