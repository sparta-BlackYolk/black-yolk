package com.sparta.blackyolk.logistic_service.hub.application.domain;

import lombok.Builder;

@Builder
public record AddressForUpdateHub(
    String sido,
    String sigungu,
    String eupmyun,
    String roadName,
    String buildingNumber,
    String zipCode
) {

}
