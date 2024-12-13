package com.sparta.blackyolk.logistic_service.hubroute.application.domain;

public record HubRouteForCreate(
    Long userId,
    String role,
    String departureHubId,
    String arrivalHubId,
    String targetHubCenter,
    String timeSlot,
    double timeSlotWeight
) {

}
