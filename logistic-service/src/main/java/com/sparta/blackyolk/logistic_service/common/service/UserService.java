package com.sparta.blackyolk.logistic_service.common.service;

import com.sparta.blackyolk.logistic_service.common.domain.UserResponseDto;
import com.sparta.blackyolk.logistic_service.company.application.dto.UserData;

import java.util.Optional;

public interface UserService {

    Optional<UserResponseDto> getUser(String username);
    Optional<UserData> getUserById(Long userId);
}
