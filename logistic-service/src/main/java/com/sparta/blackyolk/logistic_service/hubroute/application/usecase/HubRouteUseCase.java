package com.sparta.blackyolk.logistic_service.hubroute.application.usecase;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;

public interface HubRouteUseCase {
    HubRoute createHubRoute(HubRouteForCreate hubRouteForCreate);
}
