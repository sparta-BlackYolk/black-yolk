package com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;

public record HubRouteDeleteResponse(
    String hubRouteId
) {

    public static HubRouteDeleteResponse toDomain(HubRoute hubRoute) {
        return new HubRouteDeleteResponse(
            hubRoute.getHubRouteId()
        );
    }
}
