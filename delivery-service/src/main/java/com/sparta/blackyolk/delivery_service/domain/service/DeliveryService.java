package com.sparta.blackyolk.delivery_service.domain.service;
import com.sparta.blackyolk.delivery_service.application.dto.request.DeliverySearchRequestDto;
import com.sparta.blackyolk.delivery_service.domain.model.DeliveryStatus;
import com.sparta.blackyolk.delivery_service.application.dto.response.DeliveryListResponseDto;
import com.sparta.blackyolk.delivery_service.application.dto.response.DeliveryDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DeliveryService {
    DeliveryDetailResponseDto getDeliveryById(UUID deliveryId);

    UUID createDelivery();

    UUID updateDelivery(UUID deliveryId, DeliveryStatus status);

    UUID deleteDelivery(UUID deliveryId);

    Page<DeliveryListResponseDto> getDeliveries(Pageable pageable, DeliverySearchRequestDto requestDto);

    UUID getDeliveryByOrderId(UUID orderId);

    UUID deleteDeliveryByOrderId(UUID orderId);
}