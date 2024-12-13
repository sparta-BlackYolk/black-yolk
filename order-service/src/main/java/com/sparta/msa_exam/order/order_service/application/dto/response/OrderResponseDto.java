package com.sparta.msa_exam.order.order_service.application.dto.response;

import lombok.Builder;
import java.util.UUID;

@Builder
public record OrderResponseDto(
        UUID orderId,
        UUID deliveryId
) {}