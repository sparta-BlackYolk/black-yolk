package com.sparta.blackyolk.logistic_service.common.exception;

public record ErrorResponse(
    int statusCode,
    String code,
    String message
) {

}
