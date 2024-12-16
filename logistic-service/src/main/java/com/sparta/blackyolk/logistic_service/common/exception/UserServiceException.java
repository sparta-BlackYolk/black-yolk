package com.sparta.blackyolk.logistic_service.common.exception;

import feign.FeignException;

public class UserServiceException extends FeignException {
    public UserServiceException(int code, String message) {
        super(code, message);
    }
}
