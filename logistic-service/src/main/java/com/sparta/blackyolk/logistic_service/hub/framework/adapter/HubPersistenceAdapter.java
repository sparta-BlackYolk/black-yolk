package com.sparta.blackyolk.logistic_service.hub.framework.adapter;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.application.port.HubPersistencePort;
import com.sparta.blackyolk.logistic_service.hub.framework.repository.HubRepository;
import com.sparta.blackyolk.logistic_service.hub.persistence.HubEntity;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubPersistenceAdapter implements HubPersistencePort {

    private final HubRepository hubRepository;


    @Override
    public Hub saveHub(HubForCreate hubForCreate, BigDecimal axisX, BigDecimal axisY) {
        return hubRepository.save(HubEntity.toEntity(hubForCreate, axisX, axisY)).toDomain();
    }
}
