package com.sparta.msa_exam.order.order_service.domain.service;

import com.sparta.msa_exam.order.order_service.application.dto.request.OrderCreateRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.request.OrderSearchRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.request.OrderUpdateRequestDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.OrderDetailResponseDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.OrderListResponseDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.OrderResponseDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.PageResponseDto;

import java.util.UUID;

public interface OrderService {
    OrderResponseDto createOrder(OrderCreateRequestDto requestDto);

    OrderResponseDto deleteOrder(UUID orderId);

    OrderDetailResponseDto getOrderById(UUID orderId);

    PageResponseDto<OrderListResponseDto> getOrders(OrderSearchRequestDto requestDto);

    OrderResponseDto updateOrder(UUID orderId, OrderUpdateRequestDto requestDto);
}