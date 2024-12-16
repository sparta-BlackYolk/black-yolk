package com.sparta.blackyolk.auth_service.client;

import lombok.Getter;

@Getter
public class HubGetResponse {
    private String hubId;
    private String hubName;
    private String hubCenter;
    private String hubManagerId; // 허브 관리자 ID
}
