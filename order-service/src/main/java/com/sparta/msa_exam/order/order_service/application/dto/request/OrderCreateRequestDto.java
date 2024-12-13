package com.sparta.msa_exam.order.order_service.application.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderCreateRequestDto(
        UUID requestCompanyId,
        UUID productId,
        Integer quantity,
        Integer price,
        String remarks) {

}