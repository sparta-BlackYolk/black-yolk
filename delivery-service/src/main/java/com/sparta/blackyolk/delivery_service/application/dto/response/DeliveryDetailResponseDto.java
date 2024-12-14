package com.sparta.blackyolk.delivery_service.application.dto.response;

import com.sparta.blackyolk.delivery_service.domain.model.Delivery;
import com.sparta.blackyolk.delivery_service.domain.model.DeliveryStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record DeliveryDetailResponseDto(
        UUID deliveryId,
        UUID orderId,
        DeliveryStatus deliveryStatus,
        UUID originHubId,
        String originHubName,
        UUID destinationHubId,
        String destinationHubName,
        String recipientName,
        String deliveryAddress,
        List<DeliveryRouteResponseDto> deliveryRoutes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static DeliveryDetailResponseDto fromEntity(Delivery delivery){
        return DeliveryDetailResponseDto.builder()
                .deliveryId(delivery.getId())
                .orderId(delivery.getOrderId())
                .deliveryStatus(delivery.getCurrentStatus())
                .originHubId(delivery.getOriginHubId())
                .originHubName("출발 허브")
                .destinationHubId(delivery.getDestinationHubId())
                .destinationHubName("도착 허브")
                .recipientName(delivery.getReceiverName())
                .deliveryAddress(delivery.getDeliveryAddress())
                .deliveryRoutes(null)
                .createdAt(delivery.getCreatedAt())
                .updatedAt(delivery.getUpdatedAt())
                .build();
    }
}