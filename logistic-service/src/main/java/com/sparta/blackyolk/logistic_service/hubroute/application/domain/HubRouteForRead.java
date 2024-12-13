package com.sparta.blackyolk.logistic_service.hubroute.application.domain;

public record HubRouteForRead(
    Long userId,
    String departureHubId,
    String hubRouteId
) {

}
