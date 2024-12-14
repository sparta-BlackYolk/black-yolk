package com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hub.framework.web.validation.ValidCenter;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;
import jakarta.validation.constraints.NotBlank;

public record HubRouteCreateRequest(
    @NotBlank(message = "연결 허브를 입력 해주세요.")
    String targetHubId,

    @NotBlank(message = "연결 허브 센터를 입력해주세요.")
    @ValidCenter
    String targetHubCenter
) {

    public static HubRouteForCreate toDomain(
        Long userId,
        String role,
        String hubId,
        HubRouteCreateRequest hubRouteCreateRequest
    ) {
        return new HubRouteForCreate(
            userId,
            role,
            hubId,
            hubRouteCreateRequest.targetHubId,
            hubRouteCreateRequest.targetHubCenter
        );
    }
}
