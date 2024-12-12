package com.sparta.blackyolk.logistic_service.hub.application.usecase;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForDelete;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForUpdate;

public interface HubUseCase {
    Hub createHub(HubForCreate hubForCreate);
    Hub updateHub(HubForUpdate hubForUpdate);
    Hub deleteHub(HubForDelete hubForDelete);
}
