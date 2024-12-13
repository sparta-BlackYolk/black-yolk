package com.sparta.msa_exam.order.order_service.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeliveryListResponseDto(
        UUID deliveryId,
        UUID orderId,
        String currentStatus,
        UUID sourceHubId,
        String sourceHubName,
        UUID destinationHubId,
        String destinationHubName,
        String receiverName,
        String deliveryAddress,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}