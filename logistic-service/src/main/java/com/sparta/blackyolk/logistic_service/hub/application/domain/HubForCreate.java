package com.sparta.blackyolk.logistic_service.hub.application.domain;

public record HubForCreate(
    String userId,
    String role,
    String authorization,
    String hubManagerId,
    String name,
    String center,
    String sido,
    String sigungu,
    String eupmyun,
    String roadName,
    String buildingNumber,
    String zipCode
) {

}
