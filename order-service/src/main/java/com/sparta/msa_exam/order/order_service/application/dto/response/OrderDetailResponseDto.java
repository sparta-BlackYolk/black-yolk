package com.sparta.msa_exam.order.order_service.application.dto.response;

import com.sparta.msa_exam.order.order_service.domain.model.Order;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record OrderDetailResponseDto(
        UUID orderId,
        UUID productId,
        UUID requestCompanyId,
        UUID supplyCompanyId,
        String orderStatus,
        Integer quantity,
        LocalDateTime deliveryDeadline,
        String remarks,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static OrderDetailResponseDto from(Order order) {
        return new OrderDetailResponseDto(
                order.getId(),
                order.getProductId(),
                order.getRequestCompanyId(),
                order.getSupplyCompanyId(),
                order.getOrderStatus().toString(),
                order.getQuantity(),
                order.getDeliveryDeadline(),
                order.getRemarks(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}