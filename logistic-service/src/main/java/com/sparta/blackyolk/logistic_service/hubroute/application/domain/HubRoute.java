package com.sparta.blackyolk.logistic_service.hubroute.application.domain;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HubRoute implements Serializable {

    private String hubRouteId;
    private Hub departureHub;
    private Hub arrivalHub;
    private HubRouteStatus status;
    private Long duration;
    private Long distance;

    public HubRoute(
        Hub departureHub,
        Hub arrivalHub,
        Long distance,
        Long duration
    ) {
        this.departureHub = departureHub;
        this.arrivalHub = arrivalHub;
        this.distance = distance;
        this.duration = duration;
    }

    public boolean isDepartureHubBelongToHubRoute(String departureHubId) {
        return departureHubId.equals(departureHub.getHubId());
    }

}
