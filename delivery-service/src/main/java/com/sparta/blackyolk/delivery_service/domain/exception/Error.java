package com.sparta.blackyolk.delivery_service.domain.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Error {

    NOT_FOUND_DELIVERY(HttpStatus.NOT_FOUND, "해당하는 배송을 찾을 수 없습니다. ");

    private final HttpStatus status;
    private final String message;
}
