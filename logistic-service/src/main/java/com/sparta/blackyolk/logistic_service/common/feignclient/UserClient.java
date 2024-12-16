package com.sparta.blackyolk.logistic_service.common.feignclient;

import com.sparta.blackyolk.logistic_service.common.config.FeignConfig;
import com.sparta.blackyolk.logistic_service.common.domain.UserResponseDto;
import com.sparta.blackyolk.logistic_service.common.service.UserService;
import java.util.Optional;

import com.sparta.blackyolk.logistic_service.company.application.dto.UserData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service" , configuration = FeignConfig.class)
public interface UserClient extends UserService {

    @GetMapping("/api/auth/users/{username}")
    Optional<UserResponseDto> getUser(
        @PathVariable(value = "username") String username,
        @RequestHeader(value = "Authorization") String authorization
    );

    @GetMapping("/api/auth/users/search/{userId}")
    Optional<UserData> getUserById(@PathVariable Long userId);
}
