package com.sparta.msa_exam.order.order_service.application.dto.request;

import com.sparta.msa_exam.order.order_service.domain.model.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record OrderUpdateRequestDto (
        @NotNull(message = "주문 상태는 필수 항목입니다.")
        OrderStatus orderStatus,

        @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
        Integer quantity,

        @Size(max = 255, message = "요청사항은 최대 255자까지 입력할 수 있습니다.")
        String remarks
) {}