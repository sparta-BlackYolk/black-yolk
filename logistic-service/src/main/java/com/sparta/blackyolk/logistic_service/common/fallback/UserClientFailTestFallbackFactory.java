package com.sparta.blackyolk.logistic_service.common.fallback;

import com.sparta.blackyolk.logistic_service.common.domain.UserResponseDto;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.common.exception.UserServiceException;
import com.sparta.blackyolk.logistic_service.common.feignclient.UserFailTestClient;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserClientFailTestFallbackFactory implements FallbackFactory<UserFailTestClient> {

    @Override
    public UserFailTestClient create(Throwable cause) {
        return new UserFailTestClient() {

            @Override
            public Optional<UserResponseDto> getUser(String username, String authorization) {
                log.error("[UserClientFailTestFallbackFactory 호출됨] 원인 : {}", cause.getMessage());

                throw new UserServiceException(
                    ErrorCode.SERVICE_UNAVAILABLE.getCode(),
                    ErrorCode.SERVICE_UNAVAILABLE.getDetailMessage()
                );
            }
        };
    }
}
