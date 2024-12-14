package com.sparta.msa_exam.order.order_service.domain.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OrderException extends RuntimeException{
    private final Error error;
}
