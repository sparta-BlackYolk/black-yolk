package com.sparta.blackyolk.logistic_service.hubroute.framework.adapter;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import com.sparta.blackyolk.logistic_service.hub.framework.repository.HubReadOnlyRepository;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForDelete;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRoutePersistencePort;
import com.sparta.blackyolk.logistic_service.hubroute.data.HubRouteEntity;
import com.sparta.blackyolk.logistic_service.hubroute.framework.repository.HubRouteReadOnlyRepository;
import com.sparta.blackyolk.logistic_service.hubroute.framework.repository.HubRouteRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class HubRoutePersistenceAdapter implements HubRoutePersistencePort {

    private final HubRouteRepository hubRouteRepository;
    private final HubReadOnlyRepository hubReadOnlyRepository;
    private final HubRouteReadOnlyRepository hubRouteReadOnlyRepository;

    @Transactional(readOnly = true)
    public Optional<HubRoute> findByHubRouteId(String hubRouteId) {
        return hubRouteReadOnlyRepository.findByHubRouteIdAndIsDeletedFalse(hubRouteId)
            .map(HubRouteEntity::toDomain)
            .or(Optional::empty);
    }

    @Transactional(readOnly = true)
    public Page<HubRouteEntity> findAllHubRoutesByHubIdWithKeyword(String hubId, String keyword, Pageable pageable) {
        return hubRouteReadOnlyRepository.findAllHubRoutesByHubIdAndIsDeletedFalseWithKeyword(
            hubId,
            keyword,
            pageable
        );
    }

    @Transactional
    @Override
    public HubRoute createHubRoute(Long userId, HubRoute hubRoute) {

        List<HubEntity> hubEntities = hubReadOnlyRepository.findByHubIdsAndIsDeletedFalse(
            List.of(hubRoute.getDepartureHub().getHubId(), hubRoute.getArrivalHub().getHubId())
        );

        HubEntity departureHubEntity = hubEntities.stream()
            .filter(hubEntity -> hubEntity.getHubId().equals(hubRoute.getDepartureHub().getHubId()))
            .findFirst().get();

        HubEntity arrivalHubEntity = hubEntities.stream()
            .filter(hubEntity -> hubEntity.getHubId().equals(hubRoute.getArrivalHub().getHubId()))
            .findFirst().get();

        return hubRouteRepository.save(HubRouteEntity.toEntity(
            userId,
            departureHubEntity,
            arrivalHubEntity,
            hubRoute
        )).toDomain();
    }

    @Transactional
    @Override
    public HubRoute updateHubRoute(HubRouteForUpdate hubRouteForUpdate, Hub arrivalHub, BigDecimal distance, Integer duration) {

        HubRouteEntity hubRouteEntity = hubRouteReadOnlyRepository.findByHubRouteIdAndIsDeletedFalse(hubRouteForUpdate.hubRouteId()).get();
        HubEntity arrivalHubEntity = arrivalHub != null ?
        hubReadOnlyRepository.findByHubIdAndIsDeletedFalse(hubRouteForUpdate.arrivalHubId()).get() : null;

        hubRouteEntity.update(hubRouteForUpdate, arrivalHubEntity, distance, duration);

        return hubRouteEntity.toDomain();
    }

    @Transactional
    @Override
    public HubRoute deleteHubRoute(HubRouteForDelete hubRouteForDelete) {

        HubRouteEntity hubRouteEntity = hubRouteReadOnlyRepository.findByHubRouteIdAndIsDeletedFalse(hubRouteForDelete.hubRouteId()).get();
        hubRouteEntity.delete(hubRouteForDelete);

        return hubRouteEntity.toDomain();
    }
}
