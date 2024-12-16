package com.sparta.blackyolk.delivery_service.application.service;

import com.sparta.blackyolk.delivery_service.application.dto.request.DeliverySearchRequestDto;
import com.sparta.blackyolk.delivery_service.application.dto.request.DeliverySearchRequestDto;
import com.sparta.blackyolk.delivery_service.application.dto.response.DeliveryListResponseDto;
import com.sparta.blackyolk.delivery_service.application.dto.response.DeliveryDetailResponseDto;
import com.sparta.blackyolk.delivery_service.domain.exception.DeliveryException;
import com.sparta.blackyolk.delivery_service.domain.exception.Error;
import com.sparta.blackyolk.delivery_service.domain.model.Delivery;
import com.sparta.blackyolk.delivery_service.domain.model.DeliveryStatus;
import com.sparta.blackyolk.delivery_service.domain.repository.DeliveryRepository;
import com.sparta.blackyolk.delivery_service.domain.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Override
    @Transactional(readOnly = true)
    public DeliveryDetailResponseDto getDeliveryById(UUID deliveryId) {

        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryException(Error.NOT_FOUND_DELIVERY)
        );

        return null;
        //return DeliveryDetailResponseDto.fromEntity(delivery);
    }

    @Override
    public UUID createDelivery() {
        return null;
    }

    @Override
    @Transactional
    public UUID updateDelivery(UUID deliveryId, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryException(Error.NOT_FOUND_DELIVERY)
        );

        delivery.updateDelivery(status);

        return deliveryId;
    }

    @Override
    @Transactional
    public UUID deleteDelivery(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryException(Error.NOT_FOUND_DELIVERY)
        );

        delivery.deleteDelivery(delivery.getCreatedBy());

        return deliveryId;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<DeliveryListResponseDto> getDeliveries(Pageable pageable, DeliverySearchRequestDto requestDto) {
        return deliveryRepository.getDeliveries(pageable, requestDto);
    }

    @Override
    public UUID getDeliveryByOrderId(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderIdAndIsDeleteFalse(orderId).orElseThrow(
                () -> new DeliveryException(Error.NOT_FOUND_DELIVERY)
        );
        return delivery.getId();
    }

    @Transactional
    @Override
    public UUID deleteDeliveryByOrderId(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderIdAndIsDeleteFalse(orderId).orElseThrow(
                () -> new DeliveryException(Error.NOT_FOUND_DELIVERY)
        );

        //TODO : USERID 받으면 수정
        delivery.deleteDelivery(orderId);

        return delivery.getId();
    }


}