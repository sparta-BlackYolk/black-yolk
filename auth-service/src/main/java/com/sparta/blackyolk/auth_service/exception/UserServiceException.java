package com.sparta.blackyolk.auth_service.exception;

import feign.FeignException;

public class UserServiceException extends FeignException {
    public UserServiceException(int code, String message) {
        super(code, message);
    }
}

