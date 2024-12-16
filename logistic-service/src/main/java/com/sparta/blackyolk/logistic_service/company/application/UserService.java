package com.sparta.blackyolk.logistic_service.company.application;

import com.sparta.blackyolk.logistic_service.company.application.dto.UserData;

import java.util.List;

public interface UserService {
    UserData getUserByDynamicCondition(Long userId, String username, String email, String slackId);
    UserData getUserById(Long userId);

}
