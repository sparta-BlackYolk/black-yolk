package com.sparta.blackyolk.logistic_service.common.feignclient;

import com.sparta.blackyolk.logistic_service.common.config.FeignConfig;
import com.sparta.blackyolk.logistic_service.common.domain.UserResponseDto;
import com.sparta.blackyolk.logistic_service.common.fallback.UserClientFallbackFactory;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
    name = "auth-service-fail-test",
    fallbackFactory = UserClientFallbackFactory.class,
    url = "http://invalid-url",
    configuration = FeignConfig.class
)
public interface UserFailTestClient {

    @GetMapping("/api/auth/users/{username}")
    Optional<UserResponseDto> getUser(
        @PathVariable(value = "username") String username,
        @RequestHeader(value = "Authorization") String authorization
    );
}
