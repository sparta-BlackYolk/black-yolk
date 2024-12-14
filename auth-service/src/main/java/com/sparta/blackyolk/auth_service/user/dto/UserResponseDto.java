package com.sparta.blackyolk.auth_service.user.dto;

import com.sparta.blackyolk.auth_service.user.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String slackId;
    private UserRoleEnum role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}