package com.sparta.blackyolk.logistic_service.hub.data.vo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum HubStatus {
    ACTIVE("활성"),
    INACTIVE("비활성");

    private final String message;
}
