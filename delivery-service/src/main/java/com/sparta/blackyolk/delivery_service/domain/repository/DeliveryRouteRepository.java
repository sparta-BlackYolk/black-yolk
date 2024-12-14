package com.sparta.blackyolk.delivery_service.domain.repository;

import com.sparta.blackyolk.delivery_service.domain.model.DeliveryRoute;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRouteRepository {

    DeliveryRoute save(DeliveryRoute deliveryRoute);

    Optional<DeliveryRoute> findById(UUID deliveryRouteId);

    // 특정 배송의 경로 조회 - 여러 개의 경로가 존재할 수 있음
    List<DeliveryRoute> findAllByDeliveryId(UUID deliveryId);

    void delete(DeliveryRoute deliveryRoute);
}
