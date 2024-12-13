package com.sparta.blackyolk.logistic_service.hubroute.data.vo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum HubRouteStatus {
    ACTIVE("활성"),
    INACTIVE("비활성");

    private final String message;
}
