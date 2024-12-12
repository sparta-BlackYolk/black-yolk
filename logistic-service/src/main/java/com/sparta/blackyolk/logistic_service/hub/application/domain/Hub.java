package com.sparta.blackyolk.logistic_service.hub.application.domain;

import com.sparta.blackyolk.logistic_service.hub.data.vo.HubStatus;
import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubAddress;
import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubCoordinate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Hub {
    private final String hubId;
    private final String hubName;
    private final HubStatus hubStatus;
    private final HubCoordinate hubCoordinate;
    private final HubAddress hubAddress;
    private final Long hubManagerId;
    private boolean isDeleted;
}
