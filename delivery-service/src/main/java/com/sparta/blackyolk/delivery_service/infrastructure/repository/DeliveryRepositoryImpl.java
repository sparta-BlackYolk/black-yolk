package com.sparta.blackyolk.delivery_service.infrastructure.repository;

import com.sparta.blackyolk.delivery_service.domain.model.Delivery;
import com.sparta.blackyolk.delivery_service.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {
    private final DeliveryJpaRepository deliveryJpaRepository;

    @Override
    public Delivery save(Delivery delivery) {
        return deliveryJpaRepository.save(delivery);
    }

    @Override
    public Optional<Delivery> findById(UUID deliveryId) {
        return deliveryJpaRepository.findById(deliveryId);
    }

    @Override
    public Optional<Delivery> findByOrderId(UUID orderId) {
        return deliveryJpaRepository.findByOrderId(orderId);
    }

    @Override
    public void delete(Delivery delivery) {
        delivery.delete();
        deliveryJpaRepository.save(delivery);
    }
}
