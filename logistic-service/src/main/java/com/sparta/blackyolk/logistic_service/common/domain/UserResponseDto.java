package com.sparta.blackyolk.logistic_service.common.domain;

import com.sparta.blackyolk.logistic_service.common.domain.vo.UserRoleEnum;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String slackId;
    private UserRoleEnum role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
