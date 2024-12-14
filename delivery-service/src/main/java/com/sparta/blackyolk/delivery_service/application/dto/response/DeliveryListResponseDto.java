package com.sparta.blackyolk.delivery_service.application.dto.response;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record DeliveryListResponseDto(
        UUID deliveryId,
        UUID orderId,
        String deliveryStatus,
        UUID originHubId,
        String originHubName,
        UUID destinationHubId,
        String destinationHubName,
        String recipientName,
        String deliveryAddress,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
