package com.sparta.blackyolk.delivery_service.infrastructure.repository;

import com.sparta.blackyolk.delivery_service.domain.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, UUID> {
    // 주문 ID로 배송 조회 - 하나의 주문에 하나의 배송이 연결
    Optional<Delivery> findByOrderId(UUID orderId);
}
