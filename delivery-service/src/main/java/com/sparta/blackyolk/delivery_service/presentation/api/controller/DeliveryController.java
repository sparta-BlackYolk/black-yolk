package com.sparta.blackyolk.delivery_service.presentation.api.controller;

import com.sparta.blackyolk.delivery_service.application.dto.request.DeliverySearchRequestDto;
import com.sparta.blackyolk.delivery_service.application.dto.response.DeliveryDetailResponseDto;
import com.sparta.blackyolk.delivery_service.application.dto.response.DeliveryListResponseDto;
import com.sparta.blackyolk.delivery_service.domain.model.DeliveryStatus;
import com.sparta.blackyolk.delivery_service.presentation.api.controller.docs.DeliveryControllerDocs;
import com.sparta.blackyolk.delivery_service.presentation.api.response.Response;
import com.sparta.blackyolk.delivery_service.domain.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController extends DeliveryControllerDocs {

    private final DeliveryService deliveryService;

    @Override
    @GetMapping("/{deliveryId}")
    public Response<DeliveryDetailResponseDto> getDeliveryById(@PathVariable UUID deliveryId){
        return new Response<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), deliveryService.getDeliveryById(deliveryId));
    }

    @Override
    @PostMapping("")
    public Response<UUID> createDelivery(){
        return new Response<>(HttpStatus.CREATED.value(),HttpStatus.CREATED.getReasonPhrase(),deliveryService.createDelivery());
    }

    @Override
    @PatchMapping("/{deliveryId}")
    public Response<UUID> updateDelivery(@PathVariable UUID deliveryId, @RequestBody DeliveryStatus status){
        return new Response<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),deliveryService.updateDelivery(deliveryId, status));
    }

    @Override
    @DeleteMapping("/{deliveryId}")
    public Response<UUID> deleteDelivery(@PathVariable UUID deliveryId){
        return new Response<>(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.getReasonPhrase(), deliveryService.deleteDelivery(deliveryId));
    }

    @Override
    @GetMapping()
    public Response<Page<DeliveryListResponseDto>> getDeliveries(Pageable pageable, DeliverySearchRequestDto requestDto) {
        return new Response<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), deliveryService.getDeliveries(pageable, requestDto));
    }

    @GetMapping("/orders/{orderId}")
    @Override
    public UUID getDeliveryByOrderId(@PathVariable UUID orderId) {
        return deliveryService.getDeliveryByOrderId(orderId);
    }

    @DeleteMapping("/orders/{orderId}")
    @Override
    public UUID deleteDeliveryByOrderId(@PathVariable UUID orderId) {
        return deliveryService.deleteDeliveryByOrderId(orderId);
    }


}