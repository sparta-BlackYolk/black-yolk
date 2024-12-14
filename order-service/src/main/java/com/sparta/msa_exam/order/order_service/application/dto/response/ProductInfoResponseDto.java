package com.sparta.msa_exam.order.order_service.application.dto.response;

import java.util.UUID;

public record ProductInfoResponseDto(
        UUID productId,
        String productName,
        UUID supplyCompanyId
) {
}
