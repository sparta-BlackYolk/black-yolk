package com.sparta.blackyolk.logistic_service.hub.application.domain;

public record HubForCreate(
    Long userId,
    Long hubManagerId,
    String name,
    String sido,
    String sigungu,
    String eupmyun,
    String roadName,
    String buildingNumber,
    String zipCode
) {

}
