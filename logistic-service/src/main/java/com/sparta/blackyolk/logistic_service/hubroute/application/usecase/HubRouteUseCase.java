package com.sparta.blackyolk.logistic_service.hubroute.application.usecase;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForDelete;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;

public interface HubRouteUseCase {
    HubRoute createHubRoute(HubRouteForCreate hubRouteForCreate);
    HubRoute updateHubRoute(HubRouteForUpdate hubRouteForUpdate);
    HubRoute deleteHubRoute(HubRouteForDelete hubRouteForDelete);
}
