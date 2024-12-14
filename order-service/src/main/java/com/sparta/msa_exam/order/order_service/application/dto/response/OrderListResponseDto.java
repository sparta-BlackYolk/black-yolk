package com.sparta.msa_exam.order.order_service.application.dto.response;

import com.sparta.msa_exam.order.order_service.domain.model.Order;
import com.sparta.msa_exam.order.order_service.domain.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderListResponseDto(
        UUID orderId,
        UUID productId,
        String productName,
        Integer quantity,
        UUID requestCompanyId,
        String requestCompanyName,
        OrderStatus orderStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
    public static OrderListResponseDto from(Order order, CompanyResponseDto requestCompany, ProductResponseDto product) {
        return new OrderListResponseDto(
                order.getId(),
                order.getProductId(),
                product.productName(),
                order.getQuantity(),
                order.getRequestCompanyId(),
                requestCompany.companyName(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}