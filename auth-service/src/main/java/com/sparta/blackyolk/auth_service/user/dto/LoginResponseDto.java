package com.sparta.blackyolk.auth_service.user.dto;
import com.sparta.blackyolk.auth_service.user.entity.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private Long userId; // 사용자 ID
    private UserRoleEnum role; // 사용자 역할 (ENUM 타입 유지)
}
