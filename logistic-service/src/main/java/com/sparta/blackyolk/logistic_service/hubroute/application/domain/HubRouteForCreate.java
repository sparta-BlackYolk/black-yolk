package com.sparta.blackyolk.logistic_service.hubroute.application.domain;

public record HubRouteForCreate(
    String userId,
    String role,
    String departureHubId,
    String arrivalHubId,
    String targetHubCenter
) {

}
