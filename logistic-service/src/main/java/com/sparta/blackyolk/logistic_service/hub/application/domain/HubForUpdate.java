package com.sparta.blackyolk.logistic_service.hub.application.domain;

import com.sparta.blackyolk.logistic_service.hub.data.vo.HubStatus;

public record HubForUpdate(
    Long userId,
    String role,
    String hubId,
    Long hubManagerId,
    String name,
    HubStatus status,
    AddressForUpdateHub address
) {

}
