package com.sparta.blackyolk.delivery_service.infrastructure.repository;

import com.sparta.blackyolk.delivery_service.domain.model.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryRouteJpaRepository extends JpaRepository<DeliveryRoute, UUID> {

    // 특정 배송의 모든 경유지 정보 조회
    List<DeliveryRoute> findAllByDeliveryId(UUID deliveryId);
}
