package com.sparta.blackyolk.delivery_service.presentation.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class Response<T> {

    private Integer code;
    private String message;
    private T data;

    public Response() {
        this.code = HttpStatus.OK.value();
        this.message = HttpStatus.OK.getReasonPhrase();
    }

    public Response(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}