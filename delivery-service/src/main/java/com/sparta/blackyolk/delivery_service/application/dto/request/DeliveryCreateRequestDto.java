package com.sparta.blackyolk.delivery_service.application.dto.request;

import java.util.UUID;

public record DeliveryCreateRequestDto(
        UUID orderId,
        UUID requestCompanyId,
        UUID supplyCompanyId,
        UUID hubId,
        String receiverName,
        String receiverAddress,        // 수취인 주소
        String receiverSlackId,        // 수취인 Slack ID
        String remarks                 // 배송 특이 사항
) {}