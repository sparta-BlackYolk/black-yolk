package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hub.data.vo.HubStatus;
import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;

public record HubCreateResponse(
    String id,
    String hubManagerId,
    String name,
    String center,
    HubStatus status,
    HubCoordinateResponse coordinate,
    HubAddressResponse address
) {

    public static HubCreateResponse toDTO(Hub hub) {
        return new HubCreateResponse(
            hub.getHubId(),
            hub.getHubManagerId(),
            hub.getHubName(),
            hub.getHubCenter(),
            hub.getHubStatus(),
            HubCoordinateResponse.fromDomain(hub.getHubCoordinate()),
            HubAddressResponse.fromDomain(hub.getHubAddress())
        );
    }
}
