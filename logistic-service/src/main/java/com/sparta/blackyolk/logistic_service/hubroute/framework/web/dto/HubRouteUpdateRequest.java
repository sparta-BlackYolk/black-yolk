package com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.application.util.TimeSlotWeightMapper;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.validation.ValidTimeSlot;

public record HubRouteUpdateRequest(
    String targetHubId,

    @ValidTimeSlot
    String timeSlot,

    HubRouteStatus status
) {

    public static HubRouteForUpdate toDomain(
        Long userId,
        String departureHubId,
        String hubRouteId,
        HubRouteUpdateRequest hubRouteUpdateRequest
    ) {
        Double weight = hubRouteUpdateRequest.timeSlot != null ?
            TimeSlotWeightMapper.getWeight(hubRouteUpdateRequest.timeSlot) : null;

        return new HubRouteForUpdate(
            userId,
            hubRouteId,
            departureHubId,
            hubRouteUpdateRequest.targetHubId,
            hubRouteUpdateRequest.timeSlot,
            weight,
            hubRouteUpdateRequest.status
        );
    }
}
