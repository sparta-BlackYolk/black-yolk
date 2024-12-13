package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record HubCreateRequest(

    Long hubManagerId,

    @NotBlank(message = "허브 명을 입력해주세요.")
    String name,

    @Valid
    HubAddressCreateRequest address
) {

}
