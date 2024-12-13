package com.sparta.blackyolk.logistic_service.hubroute.application.domain;

import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;

public record HubRouteForUpdate(
    Long userId,
    String hubRouteId,
    String departureHubId,
    String arrivalHubId,
    String timeSlot,
    Double timeSlotWeight, // timeslot이 없으면 null 처리를 할 수 있도록 Double로 설정,
    HubRouteStatus status
) {

}
