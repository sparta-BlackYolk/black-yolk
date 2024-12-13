package com.sparta.msa_exam.order.order_service.application.dto.response;

import com.sparta.msa_exam.order.order_service.domain.model.Order;
import com.sparta.msa_exam.order.order_service.domain.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderListResponseDto(
        UUID orderId,                  // 주문 ID
        UUID productId,                // 상품 ID
        String productName,            // 상품명
        Integer quantity,              // 주문 수량
        UUID requestCompanyId,         // 요청 업체 ID
        String requestCompanyName,     // 요청 업체명
        UUID supplyCompanyId,          // 공급 업체 ID
        String supplyCompanyName,      // 공급 업체명
        OrderStatus orderStatus,       // 주문 상태 (RECEIVED, IN_PROGRESS, COMPLETED, CANCELED)
        LocalDateTime createdAt,       // 주문 생성일시
        LocalDateTime updatedAt        // 주문 수정일시
) {
}