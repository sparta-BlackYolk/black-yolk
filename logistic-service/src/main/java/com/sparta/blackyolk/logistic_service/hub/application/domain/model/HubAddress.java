package com.sparta.blackyolk.logistic_service.hub.application.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HubAddress {
    private final String sido;
    private final String sigungu;
    private final String eupmyun;
    private final String roadName;
    private final String buildingNumber;
    private final String zipCode;
}
