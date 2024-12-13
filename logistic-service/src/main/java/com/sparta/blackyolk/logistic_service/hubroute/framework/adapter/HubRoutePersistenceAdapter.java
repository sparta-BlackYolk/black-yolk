package com.sparta.blackyolk.logistic_service.hubroute.framework.adapter;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import com.sparta.blackyolk.logistic_service.hub.framework.repository.HubRepository;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForDelete;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRoutePersistencePort;
import com.sparta.blackyolk.logistic_service.hubroute.data.HubRouteEntity;
import com.sparta.blackyolk.logistic_service.hubroute.framework.repository.HubRouteRepository;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class HubRoutePersistenceAdapter implements HubRoutePersistencePort {

    private final HubRouteRepository hubRouteRepository;
    private final HubRepository hubRepository;

    public Optional<HubRoute> findByHubRouteId(String hubRouteId) {
        return hubRouteRepository.findByHubRouteIdAndIsDeletedFalse(hubRouteId)
            .map(HubRouteEntity::toDomain)
            .or(Optional::empty);
    }

    @Override
    public HubRoute createHubRoute(HubRouteForCreate hubRouteForCreate, BigDecimal distance, Integer duration) {

        HubEntity departureHubEntity = hubRepository.findByHubIdAndIsDeletedFalse(hubRouteForCreate.departureHubId()).get();
        HubEntity arrivalHubEntity = hubRepository.findByHubIdAndIsDeletedFalse(hubRouteForCreate.arrivalHubId()).get();

        return hubRouteRepository.save(HubRouteEntity.toEntity(
            hubRouteForCreate,
            departureHubEntity,
            arrivalHubEntity,
            distance,
            duration
        )).toDomain();
    }

    @Transactional
    @Override
    public HubRoute updateHubRoute(HubRouteForUpdate hubRouteForUpdate, Hub arrivalHub, BigDecimal distance, Integer duration) {

        HubRouteEntity hubRouteEntity = hubRouteRepository.findByHubRouteIdAndIsDeletedFalse(hubRouteForUpdate.hubRouteId()).get();
        HubEntity arrivalHubEntity = arrivalHub != null ?
        hubRepository.findByHubIdAndIsDeletedFalse(hubRouteForUpdate.arrivalHubId()).get() : null;

        hubRouteEntity.update(hubRouteForUpdate, arrivalHubEntity, distance, duration);

        return hubRouteEntity.toDomain();
    }

    @Transactional
    @Override
    public HubRoute deleteHubRoute(HubRouteForDelete hubRouteForDelete) {

        HubRouteEntity hubRouteEntity = hubRouteRepository.findByHubRouteIdAndIsDeletedFalse(hubRouteForDelete.hubRouteId()).get();
        hubRouteEntity.delete(hubRouteForDelete);

        return hubRouteEntity.toDomain();
    }
}
