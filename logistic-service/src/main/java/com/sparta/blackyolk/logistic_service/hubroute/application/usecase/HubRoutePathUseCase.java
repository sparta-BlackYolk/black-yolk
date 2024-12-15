package com.sparta.blackyolk.logistic_service.hubroute.application.usecase;

import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRoutePathResponse;

public interface HubRoutePathUseCase {
    HubRoutePathResponse getShortestPath(String departure, String arrival, String currentTimeSlot);
}
