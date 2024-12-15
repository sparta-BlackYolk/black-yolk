package com.sparta.blackyolk.logistic_service.hubroute.application.domain;

public record HubRouteForDelete(
    String userId,
    String role,
    String departureHubId,
    String hubRouteId
) {

}
