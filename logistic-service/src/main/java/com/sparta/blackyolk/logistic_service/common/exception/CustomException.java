package com.sparta.blackyolk.logistic_service.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final int statusCode;
    private final String errorCode;
    private final String message;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getDetailMessage());
        this.statusCode = errorCode.getCode();
        this.errorCode = errorCode.getErrorCode();
        this.message = errorCode.getDetailMessage();
    }

    public CustomException(ErrorCode errorCode, String message) {
        this.statusCode = errorCode.getCode();
        this.errorCode = errorCode.getErrorCode();
        this.message = message;
    }
}
