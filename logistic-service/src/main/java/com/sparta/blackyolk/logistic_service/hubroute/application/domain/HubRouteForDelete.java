package com.sparta.blackyolk.logistic_service.hubroute.application.domain;

public record HubRouteForDelete(
    Long userId,
    String role,
    String departureHubId,
    String hubRouteId
) {

}
