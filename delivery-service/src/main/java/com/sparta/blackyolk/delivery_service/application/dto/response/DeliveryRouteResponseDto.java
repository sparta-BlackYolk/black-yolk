package com.sparta.blackyolk.delivery_service.application.dto.response;
import java.util.UUID;


public record DeliveryRouteResponseDto(
        UUID deliveryRouteId,
        String deliveryManagetName,
        Integer sequence,
        UUID startHubId,
        String startHubName,
        UUID endHubId,
        String endHubName,
        Double estimatedDistance,
        String estimatedDuration,//타입얘기
        Double actualDistance,
        String actualDuration,//타입얘기
        String deliveryRouteStatus
) {
}
