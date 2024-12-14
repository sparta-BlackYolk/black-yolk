package com.sparta.blackyolk.delivery_service.application.dto.response;

import java.util.UUID;
import java.time.LocalDateTime;
import com.sparta.blackyolk.delivery_service.domain.model.CurrentStatus;

public record DeliveryResponseDto(
        UUID deliveryId,
        UUID orderId,
        UUID requestCompanyId,
        UUID supplyCompanyId,
        UUID hubId,
        String receiverName,
        String receiverAddress,        // 수취인 주소
        String receiverSlackId,        // 수취인 Slack ID
        String remarks,                // 비고
        CurrentStatus currentStatus,   // 배송 상태 (HUB_WAITING, IN_DELIVERY, DELIVERED)
        Boolean isDeleted,             // 삭제 여부 (논리 삭제)
        LocalDateTime createdAt,       // 생성 일자
        LocalDateTime updatedAt,       // 수정 일자
        LocalDateTime deletedAt        // 삭제 일자 (논리 삭제 시 기록)
) {}