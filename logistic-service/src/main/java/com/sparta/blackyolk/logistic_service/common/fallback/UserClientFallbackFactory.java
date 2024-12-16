package com.sparta.blackyolk.logistic_service.common.fallback;

import com.sparta.blackyolk.logistic_service.common.domain.UserResponseDto;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.common.exception.UserServiceException;
import com.sparta.blackyolk.logistic_service.common.feignclient.UserClient;
import com.sparta.blackyolk.logistic_service.company.application.dto.UserData;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {

    @Override
    public UserClient create(Throwable cause) {
        return new UserClient() {
            @Override
            public Optional<UserResponseDto> getUser(String username, String authorization) {

                log.error("[[UserClientFallbackFactory 호출됨] : {}", cause.getMessage());

                throw new UserServiceException(
                    ErrorCode.SERVICE_UNAVAILABLE.getCode(),
                    ErrorCode.SERVICE_UNAVAILABLE.getDetailMessage()
                );
            }

            @Override
            public Optional<UserData> getUserById(Long userId) {
                return Optional.empty();
            }
        };
    }
}
