package com.sparta.blackyolk.logistic_service.hubroute.application.domain;

import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;

public record HubRouteForUpdate(
    Long userId,
    String role,
    String hubRouteId,
    String departureHubId, // timeslot이 없으면 null 처리를 할 수 있도록 Double로 설정,
    HubRouteStatus status
) {

}
