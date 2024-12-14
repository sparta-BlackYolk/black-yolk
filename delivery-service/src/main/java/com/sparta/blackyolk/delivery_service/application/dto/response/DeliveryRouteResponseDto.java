package com.sparta.blackyolk.delivery_service.application.dto.response;

import java.util.UUID;
import java.time.LocalDateTime;
import com.sparta.blackyolk.delivery_service.domain.model.CurrentStatus;

public record DeliveryRouteResponseDto(
        UUID deliveryRouteId,          // 배송 경로 ID (PK)
        UUID deliveryId,               // 배송 ID (Delivery와 연관 관계)
        UUID hubId,
        String remarks,
        CurrentStatus currentStatus,   // 현재 상태 (HUB_WAITING, IN_DELIVERY, DELIVERED)
        Boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {}