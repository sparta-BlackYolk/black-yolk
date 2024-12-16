package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.framework.web.validation.ValidCenter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record HubCreateRequest(

    String hubManagerId,

    @NotBlank(message = "허브 명을 입력해주세요.")
    String name,

    @NotBlank(message = "허브 센터를 입력해주세요.")
    @ValidCenter
    String center,

    @Valid
    HubAddressCreateRequest address
) {

    public static HubForCreate toDomain(
        String userId,
        String role,
        HubCreateRequest hubCreateRequest,
        String authorization
    ) {
        return new HubForCreate(
            userId,
            role,
            authorization,
            hubCreateRequest.hubManagerId(),
            hubCreateRequest.name(),
            hubCreateRequest.center(),
            hubCreateRequest.address().sido(),
            hubCreateRequest.address().sigungu(),
            hubCreateRequest.address().eupmyun(),
            hubCreateRequest.address().roadName(),
            hubCreateRequest.address().buildingNumber(),
            hubCreateRequest.address().zipCode()
        );
    }
}
