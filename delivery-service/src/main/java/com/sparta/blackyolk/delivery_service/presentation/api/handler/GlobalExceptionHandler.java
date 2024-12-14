package com.sparta.blackyolk.delivery_service.presentation.api.handler;

import com.sparta.blackyolk.delivery_service.domain.exception.DeliveryException;
import com.sparta.blackyolk.delivery_service.presentation.api.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DeliveryException.class)
    public Response<Void> handleOrderException(DeliveryException ex) {
        return new Response<>(ex.getError().getStatus().value(), ex.getError().getMessage());
    }

}