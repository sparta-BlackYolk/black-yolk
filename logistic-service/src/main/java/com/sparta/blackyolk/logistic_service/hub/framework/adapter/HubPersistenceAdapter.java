package com.sparta.blackyolk.logistic_service.hub.framework.adapter;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForUpdate;
import com.sparta.blackyolk.logistic_service.hub.application.port.HubPersistencePort;
import com.sparta.blackyolk.logistic_service.hub.framework.repository.HubRepository;
import com.sparta.blackyolk.logistic_service.hub.persistence.HubEntity;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubPersistenceAdapter implements HubPersistencePort {

    private final HubRepository hubRepository;

    public Optional<Hub> findByHubId(String hubId) {
        return hubRepository.findById(hubId)
            .map(HubEntity::toDomain)
            .or(Optional::empty);
    }

    @Override
    public Hub saveHub(HubForCreate hubForCreate, BigDecimal axisX, BigDecimal axisY) {
        return hubRepository.save(HubEntity.toEntity(hubForCreate, axisX, axisY)).toDomain();
    }

    @Override
    public Hub updateHub(HubForUpdate hubForUpdate, BigDecimal axisX, BigDecimal axisY) {

        HubEntity hubEntity = hubRepository.findById(hubForUpdate.hubId()).get();
        hubEntity.updateHub(hubForUpdate, axisX, axisY);

        return hubEntity.toDomain();
    }
}
