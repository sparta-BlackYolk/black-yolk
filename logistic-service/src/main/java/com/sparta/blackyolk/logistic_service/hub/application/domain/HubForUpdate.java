package com.sparta.blackyolk.logistic_service.hub.application.domain;

import com.sparta.blackyolk.logistic_service.hub.persistence.vo.HubStatus;

public record HubForUpdate(
    Long userId,
    String hubId,
    Long hubManagerId,
    String name,
    HubStatus status,
    AddressForUpdateHub address
) {

}
