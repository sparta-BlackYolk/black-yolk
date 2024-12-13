package com.sparta.msa_exam.order.order_service.application.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderCreateResponseDto (
        UUID orderId,
        UUID deliveryId,
        LocalDateTime createdAt,
        String orderStatus,
        String message
) {}