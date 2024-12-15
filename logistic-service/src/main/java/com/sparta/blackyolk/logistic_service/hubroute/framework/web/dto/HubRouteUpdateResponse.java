package com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;

public record HubRouteUpdateResponse(
    String hubRouteId,
    String departureHubName,
    String arrivalHubName,
    HubRouteStatus hubRouteStatus,
    Long distance,
    Long duration
) {
    public static HubRouteUpdateResponse toDTO(
        HubRoute hubRoute
    ) {
        return new HubRouteUpdateResponse(
            hubRoute.getHubRouteId(),
            hubRoute.getDepartureHub().getHubName(),
            hubRoute.getArrivalHub().getHubName(),
            hubRoute.getStatus(),
            hubRoute.getDistance(),
            hubRoute.getDuration()
        );
    }
}
