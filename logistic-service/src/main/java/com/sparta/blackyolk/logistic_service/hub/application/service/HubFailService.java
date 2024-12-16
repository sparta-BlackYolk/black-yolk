package com.sparta.blackyolk.logistic_service.hub.application.service;

import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.common.exception.UserServiceException;
import com.sparta.blackyolk.logistic_service.common.feignclient.UserFailTestClient;
import com.sparta.blackyolk.logistic_service.hub.application.usecase.HubFailUseCase;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubFailService implements HubFailUseCase {

    private final UserFailTestClient userFailTestClient;

    private static final String INVALID_USER = "invalid_user";
    private static final String INVALID_TOKEN = "invalid_token";

    public void handleFailCase() {
        log.info("[Hub 생성] Fail 케이스 실행");

        try {
            log.info("[Feign 호출] 사용자 정보 조회 실패 테스트 시작");
            // Feign 클라이언트를 호출해 RetryableException 발생 유도
            feignClientCall();
        } catch (RetryableException e) {
            log.error("[Feign 호출] 요청 실패 (RetryableException): {}", e.getMessage(), e);
            throw e; // 재시도를 위해 예외를 던짐
        } catch (Exception e) {
            log.error("[Feign 호출] 요청 실패: {}", e.getMessage(), e);
            sendFallbackNotification(e);
        }
    }

    private void feignClientCall() {
        log.info("[Feign 호출] 의도적으로 RetryableException 발생");
        userFailTestClient.getUser(INVALID_USER, INVALID_TOKEN);
    }

    private void sendFallbackNotification(Exception e) {
        log.error("[Hub 생성] Fallback 처리");
        throw new UserServiceException(
            ErrorCode.SERVICE_UNAVAILABLE.getCode(),
            ErrorCode.SERVICE_UNAVAILABLE.getDetailMessage()
        );
    }
}
