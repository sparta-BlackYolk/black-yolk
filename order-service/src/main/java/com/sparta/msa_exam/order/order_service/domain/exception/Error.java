package com.sparta.msa_exam.order.order_service.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Error {

    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "해당 주문이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}