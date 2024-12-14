package com.sparta.blackyolk.delivery_service.presentation.api.controller.docs;

import com.sparta.blackyolk.delivery_service.application.dto.request.DeliverySearchRequestDto;
import com.sparta.blackyolk.delivery_service.application.dto.response.DeliveryDetailResponseDto;
import com.sparta.blackyolk.delivery_service.application.dto.response.DeliveryListResponseDto;
import com.sparta.blackyolk.delivery_service.domain.model.DeliveryStatus;
import com.sparta.blackyolk.delivery_service.presentation.api.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Delivery", description ="배송 API" )
public abstract class DeliveryControllerDocs {


    @Operation(summary = "배송 조회(단건)", description ="배송 조회 API"  )
    @GetMapping("/deliveries/{deliveryId}")
    public abstract Response<DeliveryDetailResponseDto> getDeliveryById(@PathVariable UUID deliveryId);

    @Operation(summary = "배송 생성", description = "배송 생성 API")
    @PostMapping("/deliveries")
    public abstract Response<UUID> createDelivery();

    @Operation(summary = "배송 상태 수정", description = "배송 상태 수정 API")
    @PatchMapping("/deliveries/{deliveryId}")
    public abstract Response<UUID> updateDelivery(@PathVariable UUID deliveryId, @RequestBody DeliveryStatus status);

    @Operation(summary = "배송 삭제", description = "배송 삭제 API")
    @DeleteMapping("/deliveries/{deliveryId}")
    public abstract Response<UUID> deleteDelivery(@PathVariable UUID deliveryId);

    @Operation(summary = "배송 목록 조회(검색)", description ="배송 목록 조회(검색) API" )
    @GetMapping("/deliveries")
    public abstract Response<Page<DeliveryListResponseDto>> getDeliveries(Pageable pageable, DeliverySearchRequestDto requestDto);

    //임시 api 이름 api 규칙 질문할것
    @GetMapping("/deliveries/orders/{orderId}")
    public abstract UUID getDeliveryByOrderId(@PathVariable UUID orderId);

    @DeleteMapping("/deliveries/orders/{orderId}")
    public abstract UUID deleteDeliveryByOrderId(@PathVariable UUID orderId);

}