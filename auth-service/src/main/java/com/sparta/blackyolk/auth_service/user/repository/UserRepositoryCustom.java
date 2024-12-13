package com.sparta.blackyolk.auth_service.user.repository;

import com.sparta.blackyolk.auth_service.user.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findUserByDynamicCondition(Long userId, String username, String email, String slackId);
}
