package com.sparta.msa_exam.order.order_service.presentation.api.controller.docs;

import com.sparta.msa_exam.order.order_service.application.dto.request.OrderCreateRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.request.OrderSearchRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.request.OrderUpdateRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.OrderDetailResponseDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.OrderListResponseDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.OrderResponseDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.PageResponseDto;
import com.sparta.msa_exam.order.order_service.presentation.api.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Order", description = "주문 API")
public abstract class OrderControllerDocs {
    @Operation(summary = "주문 생성", description = "주문 생성 API")
    @PostMapping("/orders")
    public abstract Response<OrderResponseDto> createOrder(@RequestBody OrderCreateRequestDto requestDto);

    @Operation(summary = "주문 삭제", description = "주문 삭제 API")
    @DeleteMapping("/orders/{orderId}")
    public abstract Response<OrderResponseDto> deleteOrder(@PathVariable UUID orderId);

    @Operation(summary = "주문 조회(단건)", description = "주문 조회 API")
    @GetMapping("/orders/{orderId}")
    public abstract Response<OrderDetailResponseDto> getOrderById(@PathVariable UUID orderId);

    @Operation(summary = "주문 수정", description = "주문 수정 API")
    @PatchMapping("/orders/{orderId}")
    public abstract Response<OrderResponseDto> updateOrder(@PathVariable UUID orderId, @RequestBody OrderUpdateRequestDto requestDto);

    @Operation(summary = "주문 목록 조회 및 검색", description = "주문 목록 조회 및 검색 API")
    @GetMapping("/orders")
    public abstract Response<PageResponseDto<OrderListResponseDto>>getOrders(@ModelAttribute OrderSearchRequestDto requestDto);

}