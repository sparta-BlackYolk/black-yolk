package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hub.application.domain.AddressForUpdateHub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForUpdate;
import jakarta.validation.constraints.Pattern;

public record HubAddressUpdateRequest(
    String sido,
    String sigungu,
    String eupmyun,
    String roadName,
    String buildingNumber,

    @Pattern(regexp = "\\d{5}", message = "우편번호는 5자리 숫자여야 합니다.")
    String zipCode
) {

    public static HubForUpdate toDomain(
        Long userId,
        String hubId,
        HubUpdateRequest hubUpdateRequest
    ) {
        AddressForUpdateHub address = null;

        if (hubUpdateRequest.address() != null) {
            address = AddressForUpdateHub.builder()
                .sido(hubUpdateRequest.address().sido)
                .sigungu(hubUpdateRequest.address().sigungu)
                .eupmyun(hubUpdateRequest.address().eupmyun)
                .roadName(hubUpdateRequest.address().roadName)
                .buildingNumber(hubUpdateRequest.address().buildingNumber)
                .build();
        }

        return new HubForUpdate(
            userId,
            hubId,
            hubUpdateRequest.hubManagerId(),
            hubUpdateRequest.name(),
            hubUpdateRequest.status(),
            address
        );
    }

}

