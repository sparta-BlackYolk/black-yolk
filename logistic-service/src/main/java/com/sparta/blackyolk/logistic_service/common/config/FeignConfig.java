package com.sparta.blackyolk.logistic_service.common.config;

import com.sparta.blackyolk.logistic_service.common.exception.UserServiceException;
import feign.Request;
import feign.RetryableException;
import feign.Retryer;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FeignConfig {

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

            String message = errorMessages.getOrDefault(response.status(), "알 수 없는 오류가 발생했습니다.");
            return new UserServiceException(response.status(), message);
        };
    }

    public static class CustomRetryer extends Retryer.Default {
        private int attempt = 1;

        public CustomRetryer() {
            // 실패한 요청에 대해 재시도 횟수 및 재시도 간 대기 시간 설정
            super(1000, 1000, 3);
        }

        @Override
        public void continueOrPropagate(RetryableException e) {
            log.warn("[Feign 재시도] 시도 횟수: {}, 이유: {}", attempt, e.getMessage());
            attempt++;
            super.continueOrPropagate(e); // 기본 동작 유지
        }

        @Override
        public Retryer clone() {
            return new CustomRetryer();
        }
    }
}
