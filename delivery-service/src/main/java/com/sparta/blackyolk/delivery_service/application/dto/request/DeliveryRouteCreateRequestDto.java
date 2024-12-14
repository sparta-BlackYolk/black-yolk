package com.sparta.blackyolk.delivery_service.application.dto.request;


import java.util.UUID;

public record DeliveryRouteCreateRequestDto(
        UUID deliveryId,
        UUID hubId,                    // 경유 허브 ID
        UUID deliveryManagerId, // 배송담당자 ID
        String remarks
) {}

