package com.sparta.blackyolk.auth_service.config;

import com.sparta.blackyolk.auth_service.exception.UserServiceException;
import feign.Request;
import feign.RequestInterceptor;
import feign.RetryableException;
import feign.Retryer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.Map;

@Slf4j
@Configuration
public class FeignConfig {

    // Custom Retryer: 재시도 간격과 횟수를 설정
    @Bean
    public Retryer retryer() {
        return new CustomRetryer();
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(
                1000, // 연결 제한 시간 (1초)
                2000  // 읽기 제한 시간 (2초)
        );
    }

    // ErrorDecoder: 상태 코드에 따라 예외 처리, 503일 경우 재시도 예외 반환
    @Bean
    public feign.codec.ErrorDecoder errorDecoder() {
        Map<Integer, String> errorMessages = Map.of(
                400, "잘못된 요청입니다.",
                401, "인증되지 않은 사용자입니다.",
                403, "접근 권한이 없습니다.",
                404, "요청한 리소스를 찾을 수 없습니다.",
                503, "서비스를 사용할 수 없습니다. 잠시 후 다시 시도해주세요."
        );

        return (methodKey, response) -> {
            log.error("Feign 호출 실패: HTTP 상태 코드 {}, 응답: {}", response.status(), response.body());

            // 503 에러 시 RetryableException 발생
            if (response.status() == 503) {
                return new RetryableException(
                        response.status(),                      // HTTP 상태 코드
                        "서비스를 사용할 수 없습니다. 재시도 중...",  // 에러 메시지
                        response.request().httpMethod(),       // HTTP 메서드
                        new Date(),                            // 재시도 타임스탬프 (즉시 재시도)
                        response.request()                     // 요청 정보
                );
            }
            // 기타 상태 코드는 UserServiceException으로 처리
            String message = errorMessages.getOrDefault(response.status(), "알 수 없는 오류가 발생했습니다.");
            return new UserServiceException(response.status(), message);
        };
    }

    // Custom Retryer: 재시도 횟수와 재시도 간격 로그 출력
    public static class CustomRetryer extends Retryer.Default {
        private int attempt = 1;

        public CustomRetryer() {
            // 실패한 요청에 대해 재시도 횟수 및 재시도 간 대기 시간 설정
            super(1000, 1000, 3);
            log.info("CustomRetryer 생성됨: 재시도 간격={}, 최대 재시도 횟수={}", 1000, 3);
        }

        @Override
        public void continueOrPropagate(RetryableException e) {
            log.warn("[Feign 재시도] 시도 횟수: {}, 이유: {}", attempt, e.getMessage());
            attempt++;
            super.continueOrPropagate(e); // 기본 동작 유지
        }

        @Override
        public Retryer clone() {
            log.info("CustomRetryer 클론 생성됨");
            return new CustomRetryer();
        }
    }

    // RequestInterceptor: HTTP 요청 시 Authorization 헤더 추가
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
