package com.sparta.blackyolk.auth_service.courier.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CourierResponseDto {
    private UUID courierId;
    private Long userId;
    private String slackId;
    private UUID hubId;
    private String deliveryNum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
