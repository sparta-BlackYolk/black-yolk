package com.sparta.blackyolk.logistic_service.hubroute.application.usecase;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForDelete;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForRead;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteCreateResponse;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteGetResponse;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteUpdateResponse;

public interface HubRouteUseCase {
    HubRouteCreateResponse createHubRoute(HubRouteForCreate hubRouteForCreate);
    HubRouteUpdateResponse updateHubRoute(HubRouteForUpdate hubRouteForUpdate);
    HubRoute deleteHubRoute(HubRouteForDelete hubRouteForDelete);
    HubRouteGetResponse getHubRoute(HubRouteForRead hubRouteForRead);
}
