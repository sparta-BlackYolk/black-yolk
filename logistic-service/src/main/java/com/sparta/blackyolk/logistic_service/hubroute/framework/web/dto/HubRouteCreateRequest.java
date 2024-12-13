package com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;
import com.sparta.blackyolk.logistic_service.hubroute.application.util.TimeSlotWeightMapper;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.validation.ValidTimeSlot;
import jakarta.validation.constraints.NotBlank;

public record HubRouteCreateRequest(
    @NotBlank(message = "연결 허브를 입력 해주세요.")
    String targetHubId,

    @ValidTimeSlot
    String timeSlot
) {

    public static HubRouteForCreate toDomain(
        Long userId,
        String hubId,
        HubRouteCreateRequest hubRouteCreateRequest
    ) {
        return new HubRouteForCreate(
            userId,
            hubId,
            hubRouteCreateRequest.targetHubId,
            hubRouteCreateRequest.timeSlot,
            TimeSlotWeightMapper.getWeight(hubRouteCreateRequest.timeSlot)
        );
    }
}
