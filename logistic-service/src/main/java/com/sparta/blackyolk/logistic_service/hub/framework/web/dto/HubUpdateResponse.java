package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.persistence.vo.HubStatus;

public record HubUpdateResponse(
    String id,
    Long hubManagerId,
    String name,
    HubStatus status,
    HubCoordinateResponse coordinate,
    HubAddressResponse address
) {

    public static HubUpdateResponse toDTO(Hub hub) {
        return new HubUpdateResponse(
            hub.getHubId(),
            hub.getHubManagerId(),
            hub.getHubName(),
            hub.getHubStatus(),
            HubCoordinateResponse.fromDomain(hub.getHubCoordinate()),
            HubAddressResponse.fromDomain(hub.getHubAddress())
        );
    }
}
