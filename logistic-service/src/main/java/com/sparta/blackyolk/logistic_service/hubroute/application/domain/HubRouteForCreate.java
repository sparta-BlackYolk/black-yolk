package com.sparta.blackyolk.logistic_service.hubroute.application.domain;

public record HubRouteForCreate(
    Long userId,
    String departureHubId,
    String arrivalHubId,
    String timeSlot,
    double timeSlotWeight
) {

}
