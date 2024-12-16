package com.sparta.blackyolk.logistic_service.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통
    UNAUTHORIZED(401, "UNAUTHORIZED", "로그인을 해주세요."),
    FORBIDDEN(403, "USER_FORBIDDEN", "접근 권한이 없습니다."),
    REQUEST_ACCEPTED(202, "REQUEST_ACCEPTED", "잠시만 기다려주세요."),
    SERVICE_UNAVAILABLE(503, "SERVICE_UNAVAILABLE", "잠시후에 다시 요청해주세요."),

    // Hub
    HUB_BAD_REQUEST(400, "HUB_BAD_REQUEST", "잘못된 허브 요청입니다."),
    HUB_NOT_EXIST(404, "HUB_NOT_EXIST", "존재하지 않는 허브입니다."),
    HUB_ALREADY_EXIST(409, "HUB_ALREADY_EXIST", "이미 존재하는 허브입니다."),

    // HubRoute
    HUB_ROUTE_BAD_REQUEST(400, "HUB_ROUTE_BAD_REQUEST", "잘못된 허브 경로 요청입니다."),
    HUB_ROUTE_NOT_EXIST(404, "HUB_ROUTE_NOT_EXIST", "존재하지 않는 허브 경로입니다."),
    HUB_ROUTE_ALREADY_EXIST(409, "HUB_ROUTE_ALREADY_EXIST", "이미 존재하는 허브 경로입니다."),

    // Path
    PATH_ACCESS_DENIED(403, "PATH_ACCESS_DENIED", "최단 경로 조회에 접근할 수 없습니다."),
    PATH_NOT_EXIST(404, "PATH_NOT_EXIST", "존재하지 경로입니다."),

    // 외부 REST API
    URI_BAD_REQUEST(400, "URI_BAD_REQUEST", "잘못된 URI 요청입니다."),
    REST_API_FAIL(500, "REST_API_FAIL", "외부 API 요청 실패"),
    GEOCODING_API_FAIL(404, "GEOCODING_API_FAIL", "잘못된 주소 요청입니다."),

    // User
    USER_BAD_REQUEST(400, "USER_BAD_REQUEST", "잘못된 사용자 요청입니다."),
    USER_NOT_EXIST(404, "USER_NOT_EXIST", "존재하지 않는 사용자 입니다."),
    USER_ACCESS_DENIED(403, "USER_ACCESS_DENIED", "접근 권한이 없습니다."),


    // example
    INVALID_PARAMETER(400, "INVALID_PARAMETER", "클라이언트에서 요청한 파라미터의 형식이나 내용에 오류가 있는 경우"),
    INVALID_RESOURCE(400, "INVALID_{RESOURCE}", "클라이언트에서 요청한 리소스의 내용에 오류가 있는 경우"),
    ACCESS_DENIED(403, "ACCESS_DENIED", "클라이언트가 요청하는 리소스에 대한 접근이 제한되는 경우"),
    RESOURCE_NOT_EXIST(404, "{RESOURCE}_NOT_EXIST", "클라이언트가 요청한 특정 리소스를 찾을 수 없는 경우"),
    RESOURCE_ALREADY_EXIST(409, "{RESOURCE}_ALREADY_EXIST", "클라이언트가 요청한 리소스가 이미 존재하는 경우"),

    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버에 일시적인 오류가 발생했습니다."),
    CACHE_CONNECTION_ERROR(500, "CACHE_CONNECTION_ERROR", "캐시 오류");

    private final int code;
    private final String errorCode;
    private final String detailMessage;

}
