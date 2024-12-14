package com.sparta.blackyolk.delivery_service.infrastructure.repository;

import com.sparta.blackyolk.delivery_service.application.dto.request.DeliverySearchRequestDto;
import com.sparta.blackyolk.delivery_service.application.dto.response.DeliveryListResponseDto;
import com.sparta.blackyolk.delivery_service.domain.model.Delivery;
import com.sparta.blackyolk.delivery_service.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class DeliveryRepositoryImpl implements DeliveryRepository {
    private final DeliveryJpaRepository deliveryJpaRepository;

    @Override
    public Optional<Delivery> findById(UUID deliveryId) {
        return deliveryJpaRepository.findById(deliveryId);
    }

    @Override
    public Delivery save(Delivery delivery) {
        return deliveryJpaRepository.save(delivery);
    }

    @Override
    public Page<DeliveryListResponseDto> getDeliveries(Pageable pageable, DeliverySearchRequestDto requestDto) {
        return null;
    }

    @Override
    public Optional<Delivery> findByOrderIdAndIsDeleteFalse(UUID orderId) {
        return deliveryJpaRepository.findByOrderIdAndIsDeleteFalse(orderId);
    }


}
