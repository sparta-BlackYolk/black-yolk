package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record HubCreateRequest(

    Long hubManagerId,

    @NotBlank(message = "허브 명을 입력해주세요.")
    String name,

    @Valid
    HubAddressCreateRequest address
) {

    public static HubForCreate toDomain(
        Long userId,
        String role,
        HubCreateRequest hubCreateRequest
    ) {
        return new HubForCreate(
            userId,
            role,
            hubCreateRequest.hubManagerId(),
            hubCreateRequest.name(),
            hubCreateRequest.address().sido(),
            hubCreateRequest.address().sigungu(),
            hubCreateRequest.address().eupmyun(),
            hubCreateRequest.address().roadName(),
            hubCreateRequest.address().buildingNumber(),
            hubCreateRequest.address().zipCode()
        );
    }
}
