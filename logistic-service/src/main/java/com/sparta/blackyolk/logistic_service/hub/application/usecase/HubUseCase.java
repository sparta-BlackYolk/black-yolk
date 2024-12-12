package com.sparta.blackyolk.logistic_service.hub.application.usecase;


import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;

public interface HubUseCase {
    Hub createHub(HubForCreate hubForCreate);
}
