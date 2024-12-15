package com.sparta.blackyolk.logistic_service.hub.application.usecase;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForDelete;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForUpdate;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubCreateResponse;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubUpdateResponse;

public interface HubUseCase {
    HubCreateResponse createHub(HubForCreate hubForCreate);
    HubUpdateResponse updateHub(HubForUpdate hubForUpdate);
    Hub deleteHub(HubForDelete hubForDelete);
}
