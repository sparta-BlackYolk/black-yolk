package com.sparta.blackyolk.logistic_service.hubroute.application.domain;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;
import java.io.Serializable;
import java.math.BigDecimal;
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
    private Integer duration;
    private BigDecimal distance;

    public HubRoute(
        Hub departureHub,
        Hub arrivalHub
    ) {
        this.departureHub = departureHub;
        this.arrivalHub = arrivalHub;
        this.distance = calculateDistance();
        this.duration = calculateDuration();
    }

    public boolean isDepartureHubBelongToHubRoute(String departureHubId) {
        return departureHubId.equals(departureHub.getHubId());
    }

    // TODO : 코드 완성하기 - 미리 계산해놓은 것 반영시키는 코드로 작성하기
    public BigDecimal calculateDistance() {
        return new BigDecimal("50.5");
    }

    // TODO : 코드 완성하기 - 미리 계산해놓은 것 반영시키는 코드로 작성하기
    public Integer calculateDuration() {
        return 40;
    }
}
