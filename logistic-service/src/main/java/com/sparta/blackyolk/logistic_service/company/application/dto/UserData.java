package com.sparta.blackyolk.logistic_service.company.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private Long userId;
    private String username;
    private String email;
    private String slackId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
