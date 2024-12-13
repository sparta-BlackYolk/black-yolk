package com.sparta.msa_exam.order.order_service.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderCreateRequestDto (
        @NotNull(message = "요청업체 ID는 필수로 입력해야 합니다.")
        UUID requestCompanyId,

        @NotNull(message = "공급업체 ID는 필수로 입력해야 합니다.")
        UUID supplyCompanyId,

        @NotNull(message = "상품 ID는 필수로 입력해야 합니다.")
        UUID productId,

        @NotNull(message = "배송기한은 필수로 입력해야 합니다.")
        LocalDateTime deliveryDeadline,

        @NotNull
        @Min(value = 1, message = "주문 최소 수량은 1개 이상이어야 합니다.")
        Integer quantity,

        String remarks
) {}