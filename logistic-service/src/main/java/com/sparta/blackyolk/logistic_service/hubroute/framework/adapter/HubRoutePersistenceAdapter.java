package com.sparta.blackyolk.logistic_service.hubroute.framework.adapter;

import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import com.sparta.blackyolk.logistic_service.hub.framework.repository.HubReadOnlyRepository;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForDelete;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRoutePersistencePort;
import com.sparta.blackyolk.logistic_service.hubroute.data.HubRouteEntity;
import com.sparta.blackyolk.logistic_service.hubroute.framework.repository.HubRouteReadOnlyRepository;
import com.sparta.blackyolk.logistic_service.hubroute.framework.repository.HubRouteRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    public Page<HubRoute> findAllHubRoutesByHubIdWithKeyword(String hubId, String keyword, Pageable pageable) {
        return hubRouteReadOnlyRepository.findAllHubRoutesByHubIdAndIsDeletedFalseWithKeyword(
            hubId,
            keyword,
            pageable
        ).map(HubRouteEntity::toDomain);
    }

    @Transactional(readOnly = true)
    public Optional<HubRoute> findHubRouteByDepartureHubIdAndArrivalHubId(String departureHubId, String arrivalHubId) {
        return hubRouteReadOnlyRepository.findByDepartureHubIdAndArrivalHubId(departureHubId, arrivalHubId)
            .map(HubRouteEntity::toDomain)
            .or(Optional::empty);
    }

    @Override
    public List<HubRoute> findAllHubRoutes() {
        return hubRouteReadOnlyRepository.findAllHubRoutesAndIsDeletedFalseAndActive().stream()
            .map(HubRouteEntity::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<HubRoute> findAllByHubId(String hubId) {
        return hubRouteReadOnlyRepository.findAllByHubIdAndIsDeletedFalseAndActive(hubId).stream()
            .map(HubRouteEntity::toDomain)
            .collect(Collectors.toList());
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
    public HubRoute updateHubRoute(HubRouteForUpdate hubRouteForUpdate) {

        HubRouteEntity hubRouteEntity = hubRouteReadOnlyRepository.findByHubRouteIdAndIsDeletedFalse(hubRouteForUpdate.hubRouteId()).get();
        hubRouteEntity.update(hubRouteForUpdate);

        return hubRouteEntity.toDomain();
    }

    @Transactional
    @Override
    public HubRoute deleteHubRoute(HubRouteForDelete hubRouteForDelete) {

        HubRouteEntity hubRouteEntity = hubRouteReadOnlyRepository.findByHubRouteIdAndIsDeletedFalse(hubRouteForDelete.hubRouteId()).get();
        hubRouteEntity.delete(hubRouteForDelete.userId());

        return hubRouteEntity.toDomain();
    }
}
