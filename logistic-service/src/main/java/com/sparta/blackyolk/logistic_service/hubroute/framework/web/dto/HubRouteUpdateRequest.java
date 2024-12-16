package com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;

public record HubRouteUpdateRequest(

    HubRouteStatus status
) {

    public static HubRouteForUpdate toDomain(
        String userId,
        String role,
        String departureHubId,
        String hubRouteId,
        HubRouteUpdateRequest hubRouteUpdateRequest
    ) {
        return new HubRouteForUpdate(
            userId,
            role,
            hubRouteId,
            departureHubId,
            hubRouteUpdateRequest.status
        );
    }
}
