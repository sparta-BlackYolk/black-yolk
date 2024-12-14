package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hub.application.domain.AddressForUpdateHub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForUpdate;
import com.sparta.blackyolk.logistic_service.hub.data.vo.HubStatus;

public record HubUpdateRequest(
    Long hubManagerId,
    String name,
    HubStatus status,
    HubAddressUpdateRequest address
) {

    public static HubForUpdate toDomain(
        Long userId,
        String role,
        String hubId,
        HubUpdateRequest hubUpdateRequest
    ) {
        AddressForUpdateHub address = null;

        if (hubUpdateRequest.address() != null) {
            address = AddressForUpdateHub.builder()
                .sido(hubUpdateRequest.address().sido())
                .sigungu(hubUpdateRequest.address().sigungu())
                .eupmyun(hubUpdateRequest.address().eupmyun())
                .roadName(hubUpdateRequest.address().roadName())
                .buildingNumber(hubUpdateRequest.address().buildingNumber())
                .build();
        }

        return new HubForUpdate(
            userId,
            role,
            hubId,
            hubUpdateRequest.hubManagerId(),
            hubUpdateRequest.name(),
            hubUpdateRequest.status(),
            address
        );
    }
}
