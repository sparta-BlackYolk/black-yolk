package com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;
import java.math.BigDecimal;

public record HubRouteGetResponse(
    String hubRouteId,
    String departureHubName,
    String arrivalHubName,
    HubRouteStatus hubRouteStatus,
    BigDecimal distance,
    Integer duration
) {

    public static HubRouteGetResponse toDTO(
        HubRoute hubRoute
    ) {
        return new HubRouteGetResponse(
            hubRoute.getHubRouteId(),
            hubRoute.getDepartureHub().getHubName(),
            hubRoute.getArrivalHub().getHubName(),
            hubRoute.getStatus(),
            hubRoute.getDistance(),
            hubRoute.getDuration()
        );
    }
}
