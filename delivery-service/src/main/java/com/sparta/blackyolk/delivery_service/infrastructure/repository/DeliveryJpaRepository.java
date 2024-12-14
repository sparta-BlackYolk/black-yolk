package com.sparta.blackyolk.delivery_service.infrastructure.repository;

import com.sparta.blackyolk.delivery_service.domain.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, UUID> {

    Optional<Delivery> findByOrderIdAndIsDeleteFalse(UUID orderId);
}
