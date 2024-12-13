package com.sparta.blackyolk.logistic_service.hubroute.framework.adapter;

import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import com.sparta.blackyolk.logistic_service.hub.framework.repository.HubRepository;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRoutePersistencePort;
import com.sparta.blackyolk.logistic_service.hubroute.data.HubRouteEntity;
import com.sparta.blackyolk.logistic_service.hubroute.framework.repository.HubRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HubRoutePersistenceAdapter implements HubRoutePersistencePort {

    private final HubRouteRepository hubRouteRepository;
    private final HubRepository hubRepository;

    @Override
    public HubRoute createHubRoute(Long userId, HubRoute hubRoute) {

        HubEntity departureHubEntity = hubRepository.findByHubIdAndIsDeletedFalse(hubRoute.getDepartureHub().getHubId()).get();
        HubEntity arrivalHubEntity = hubRepository.findByHubIdAndIsDeletedFalse(hubRoute.getArrivalHub().getHubId()).get();

        return hubRouteRepository.save(HubRouteEntity.toEntity(userId, hubRoute, departureHubEntity, arrivalHubEntity)).toDomain();
    }
}
