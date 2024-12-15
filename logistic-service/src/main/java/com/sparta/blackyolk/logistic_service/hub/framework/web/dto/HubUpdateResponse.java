package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.data.vo.HubStatus;

public record HubUpdateResponse(
    String id,
    String hubManagerId,
    String name,
    String center,
    HubStatus status,
    HubCoordinateResponse coordinate,
    HubAddressResponse address
) {

    public static HubUpdateResponse toDTO(Hub hub) {
        return new HubUpdateResponse(
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
