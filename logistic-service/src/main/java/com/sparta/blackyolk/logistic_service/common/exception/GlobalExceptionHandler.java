package com.sparta.blackyolk.logistic_service.common.exception;

import feign.FeignException;
import feign.RetryableException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RetryableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ErrorResponse> handleRetryableException(RetryableException ex) {

        log.error("[Feign 호출 실패] 재시도 실패: {}", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            ErrorCode.SERVICE_UNAVAILABLE.getCode(),
            ErrorCode.SERVICE_UNAVAILABLE.getErrorCode(),
            ErrorCode.SERVICE_UNAVAILABLE.getDetailMessage() + " 원인: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.SERVICE_UNAVAILABLE.getCode()));
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {

        log.error("[Feign 호출 에러] 상태 코드: {}, 메시지: {}", ex.status(), ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
            ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
            ErrorCode.INTERNAL_SERVER_ERROR.getErrorCode(),
            ErrorCode.INTERNAL_SERVER_ERROR.getDetailMessage() + " 원인: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.SERVICE_UNAVAILABLE.getCode()));
    }

    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<ErrorResponse> handleUserServiceException(UserServiceException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            ErrorCode.SERVICE_UNAVAILABLE.getCode(),
            ErrorCode.SERVICE_UNAVAILABLE.getErrorCode(),
            ex.getMessage() != null ? ex.getMessage() : ErrorCode.SERVICE_UNAVAILABLE.getDetailMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ErrorCode.SERVICE_UNAVAILABLE.getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errorResponses = new ArrayList<>();

        // 유효성 검사 오류 필드를 순회하며 ErrorResponse 객체 생성
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorResponses.add(new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),  // 상태 코드
                "INVALID_PARAMETER",              // 에러 코드 (예시로 "VALIDATION_ERROR" 사용)
                error.getField() + ": " + error.getDefaultMessage() // 필드와 오류 메시지
            ));
        }

        return new ResponseEntity<>(errorResponses, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        // CustomException이 발생하면 ErrorResponse를 반환합니다.
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getStatusCode(),             // 상태 코드
            ex.getErrorCode(),              // 에러 코드
            ex.getMessage()                 // 에러 메시지
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "BAD_REQUEST",
            ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
