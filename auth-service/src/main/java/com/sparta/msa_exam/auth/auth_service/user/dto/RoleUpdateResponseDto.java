package com.sparta.msa_exam.auth.auth_service.user.dto;

import com.sparta.msa_exam.auth.auth_service.user.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RoleUpdateResponseDto {
    private Long userId;
    private UserRoleEnum role;
    private LocalDateTime updatedAt;
}