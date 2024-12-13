package com.sparta.blackyolk.logistic_service.hub.application.domain;

public record HubForDelete(
    Long userId,
    String role,
    String hubId
) {

}
