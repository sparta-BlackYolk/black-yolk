package com.sparta.blackyolk.logistic_service.hubroute.application.port;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;

public interface HubRoutePersistencePort {
    HubRoute createHubRoute(Long userId, HubRoute hubRoute);
}
