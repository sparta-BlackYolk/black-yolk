package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record HubAddressUpdateRequest(
    @NotBlank
    String sido,

    @NotBlank
    String sigungu,

    @NotBlank
    String eupmyun,

    @NotBlank
    String roadName,

    @NotBlank
    String buildingNumber,

    @NotBlank
    @Pattern(regexp = "\\d{5}", message = "우편번호는 5자리 숫자여야 합니다.")
    String zipCode
) {

}

