package com.sparta.blackyolk.logistic_service.hub.application.domain;

import com.sparta.blackyolk.logistic_service.hub.data.vo.HubStatus;

public record HubForUpdate(
    String userId,
    String role,
    String hubId,
    String hubManagerId,
    String name,
    HubStatus status,
    AddressForUpdateHub address
) {

}
