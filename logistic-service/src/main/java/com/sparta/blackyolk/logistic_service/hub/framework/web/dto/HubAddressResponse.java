package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubAddress;

public record HubAddressResponse(
    String sido,
    String sigungu,
    String eupmyun,
    String roadName,
    String buildingNumber,
    String zipCode
) {

    public static HubAddressResponse fromDomain(HubAddress hubAddress) {
        return new HubAddressResponse(
            hubAddress.getSido(),
            hubAddress.getSigungu(),
            hubAddress.getEupmyun(),
            hubAddress.getRoadName(),
            hubAddress.getBuildingNumber(),
            hubAddress.getZipCode()
        );
    }
}
