package com.sparta.blackyolk.logistic_service.hub.application.domain;

import com.sparta.blackyolk.logistic_service.hub.data.vo.HubStatus;
import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubAddress;
import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubCoordinate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Hub {
    private String hubId;
    private String hubName;
    private HubStatus hubStatus;
    private HubCoordinate hubCoordinate;
    private HubAddress hubAddress;
    private Long hubManagerId;
    private boolean isDeleted;
}
