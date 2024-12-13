package com.sparta.msa_exam.order.order_service.application.dto.request;

import com.sparta.msa_exam.order.order_service.domain.model.OrderStatus;

public record OrderUpdateRequestDto(
        OrderStatus orderStatus,
        Integer quantity,
        String remarks
) {
}