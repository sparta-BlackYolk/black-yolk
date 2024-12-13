package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record HubAddressCreateRequest(

    @NotBlank(message = "시/도를 입력해주세요.")
    String sido,

    @NotBlank(message = "시/군/구를 입력해주세요.")
    String sigungu,

    String eupmyun,

    @NotBlank(message = "도로명을 입력해주세요.")
    String roadName,

    @NotBlank(message = "건물 번호를 입력해주세요.")
    String buildingNumber,

    @NotBlank(message = "우편번호를 입력해주세요.")
    @Pattern(regexp = "\\d{5}", message = "우편번호는 5자리 숫자여야 합니다.")
    String zipCode
) {

}

