package com.sparta.blackyolk.logistic_service.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통
    UNAUTHORIZED(401, "UNAUTHORIZED", "로그인을 해주세요."),
    FORBIDDEN(403, "USER_FORBIDDEN", "접근 권한이 없습니다."),

    // Hub
    HUB_BAD_REQUEST(400, "HUB_BAD_REQUEST", "잘못된 허브 요청입니다."),
    HUB_NOT_EXIST(404, "HUB_NOT_EXIST", "존재하지 않는 허브입니다."),
    HUB_ALREADY_EXIST(409, "HUB_ALREADY_EXIST", "이미 존재하는 허브입니다."),

    // HubRoute
    HUB_ROUTE_BAD_REQUEST(400, "HUB_ROUTE_BAD_REQUEST", "잘못된 허브 경로 요청입니다."),
    HUB_ROUTE_NOT_EXIST(404, "HUB_ROUTE_NOT_EXIST", "존재하지 않는 허브 경로입니다."),
    HUB_ROUTE_ALREADY_EXIST(409, "HUB_ROUTE_ALREADY_EXIST", "이미 존재하는 허브 경로입니다."),


    // example
    INVALID_PARAMETER(400, "INVALID_PARAMETER", "클라이언트에서 요청한 파라미터의 형식이나 내용에 오류가 있는 경우"),
    INVALID_RESOURCE(400, "INVALID_{RESOURCE}", "클라이언트에서 요청한 리소스의 내용에 오류가 있는 경우"),
    ACCESS_DENIED(403, "ACCESS_DENIED", "클라이언트가 요청하는 리소스에 대한 접근이 제한되는 경우"),
    RESOURCE_NOT_EXIST(404, "{RESOURCE}_NOT_EXIST", "클라이언트가 요청한 특정 리소스를 찾을 수 없는 경우"),
    RESOURCE_ALREADY_EXIST(409, "{RESOURCE}_ALREADY_EXIST", "클라이언트가 요청한 리소스가 이미 존재하는 경우"),

    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 오류"),
    CACHE_CONNECTION_ERROR(500, "CACHE_CONNECTION_ERROR", "캐시 오류");

    private final int code;
    private final String errorCode;
    private final String detailMessage;

}
