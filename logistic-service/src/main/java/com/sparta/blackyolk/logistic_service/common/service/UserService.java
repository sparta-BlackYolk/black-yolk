package com.sparta.blackyolk.logistic_service.common.service;

import com.sparta.blackyolk.logistic_service.common.domain.UserResponseDto;
import java.util.Optional;

public interface UserService {

    Optional<UserResponseDto> getUser(String username, String authorization);
}
