package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;

public record HubDeleteResponse(
    String id
) {
    public static HubDeleteResponse toDTO(Hub hub) {
        return new HubDeleteResponse(
            hub.getHubId()
        );
    }
}
