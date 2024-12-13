package com.sparta.blackyolk.logistic_service.common.exception;

import lombok.Getter;

@Getter
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
}
