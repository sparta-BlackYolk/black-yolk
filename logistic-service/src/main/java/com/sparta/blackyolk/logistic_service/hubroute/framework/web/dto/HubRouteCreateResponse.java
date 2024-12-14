package com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;
import java.math.BigDecimal;

public record HubRouteCreateResponse(
    String hubRouteId,
    String departureHubName,
    String arrivalHubName,
    HubRouteStatus hubRouteStatus,
    BigDecimal distance,
    Integer duration
) {

    public static HubRouteCreateResponse toDTO(
        HubRoute hubRoute
    ) {
        return new HubRouteCreateResponse(
            hubRoute.getHubRouteId(),
            hubRoute.getDepartureHub().getHubName(),
            hubRoute.getArrivalHub().getHubName(),
            hubRoute.getStatus(),
            hubRoute.getDistance(),
            hubRoute.getDuration()
        );
    }
}
