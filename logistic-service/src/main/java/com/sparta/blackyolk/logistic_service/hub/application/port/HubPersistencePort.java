package com.sparta.blackyolk.logistic_service.hub.application.port;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import java.math.BigDecimal;

public interface HubPersistencePort {
    Hub saveHub(HubForCreate hubForCreate, BigDecimal axisX, BigDecimal axisY);
}
