package com.sparta.blackyolk.logistic_service.hub.application.domain.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HubAddress implements Serializable {
    private String sido;
    private String sigungu;
    private String eupmyun;
    private String roadName;
    private String buildingNumber;
    private String zipCode;
}
