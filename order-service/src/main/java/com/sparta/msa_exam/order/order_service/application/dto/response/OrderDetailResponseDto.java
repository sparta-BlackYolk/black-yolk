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
        String requestCompanyName,
        UUID supplyCompanyId,
        String supplyCompanyName,
        String productName,
        Integer quantity,
        Integer price,
        Integer totalPrice,
        UUID deliveryId,
        String orderStatus,
        String remarks,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {


    public static OrderDetailResponseDto from(Order order, CompanyResponseDto requestCompany, CompanyResponseDto supplyCompany, ProductInfoResponseDto product, UUID deliveryId) {
        return new OrderDetailResponseDto(
                order.getId(),
                product.productId(),
                supplyCompany.companyId(),
                supplyCompany.companyName(),
                supplyCompany.companyId(),
                requestCompany.companyName(),
                product.productName(),
                order.getQuantity(),
                order.getPrice(),
                order.getTotalPrice(),
                deliveryId,
                order.getStatus().toString(),
                order.getRemarks(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}