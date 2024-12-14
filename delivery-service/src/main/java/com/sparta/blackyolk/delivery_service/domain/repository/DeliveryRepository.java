package com.sparta.blackyolk.delivery_service.domain.repository;

import com.sparta.blackyolk.delivery_service.domain.model.Delivery;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository {
    Delivery save(Delivery delivery);

    Optional<Delivery> findById(UUID deliveryId);

    // 주문 ID로 배송 조회 - 하나의 주문에 하나의 배송이 연결
    Optional<Delivery> findByOrderId(UUID orderId);

    void delete(Delivery delivery);
}
