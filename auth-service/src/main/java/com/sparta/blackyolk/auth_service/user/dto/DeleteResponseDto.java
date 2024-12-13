package com.sparta.blackyolk.auth_service.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DeleteResponseDto {
    private Long userId;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;
}
