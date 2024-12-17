package com.sparta.blackyolk.logistic_service.common.feignclient;

import com.sparta.blackyolk.logistic_service.common.config.FeignConfig;
import com.sparta.blackyolk.logistic_service.common.domain.UserResponseDto;
import com.sparta.blackyolk.logistic_service.common.fallback.UserClientFallbackFactory;
import com.sparta.blackyolk.logistic_service.common.service.UserService;
import java.util.Optional;

import com.sparta.blackyolk.logistic_service.company.application.dto.UserData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "auth-service",
    fallbackFactory = UserClientFallbackFactory.class,
    configuration = FeignConfig.class
)
public interface UserClient extends UserService {

    @GetMapping("/api/auth/users/{username}")
    Optional<UserResponseDto> getUser(
        @PathVariable(value = "username") String username
    );

    @GetMapping("/api/auth/users/search/{userId}")
    Optional<UserData> getUserById(@PathVariable Long userId);
}
