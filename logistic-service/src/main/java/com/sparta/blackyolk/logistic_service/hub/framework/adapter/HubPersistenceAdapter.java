package com.sparta.blackyolk.logistic_service.hub.framework.adapter;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForDelete;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForUpdate;
import com.sparta.blackyolk.logistic_service.hub.application.port.HubPersistencePort;
import com.sparta.blackyolk.logistic_service.hub.data.vo.HubStatus;
import com.sparta.blackyolk.logistic_service.hub.framework.repository.HubReadOnlyRepository;
import com.sparta.blackyolk.logistic_service.hub.framework.repository.HubRepository;
import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import com.sparta.blackyolk.logistic_service.hubroute.data.HubRouteEntity;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;
import com.sparta.blackyolk.logistic_service.hubroute.framework.repository.HubRouteReadOnlyRepository;
import com.sparta.blackyolk.logistic_service.hubroute.framework.repository.HubRouteRepository;
import java.math.BigDecimal;
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
public class HubPersistenceAdapter implements HubPersistencePort {

    private final HubRepository hubRepository;
    private final HubRouteRepository hubRouteRepository;
    private final HubReadOnlyRepository hubReadOnlyRepository;
    private final HubRouteReadOnlyRepository hubRouteReadOnlyRepository;

    @Transactional(readOnly = true)
    public Optional<Hub> findByHubId(String hubId) {
        return hubReadOnlyRepository.findByHubIdAndIsDeletedFalse(hubId)
            .map(HubEntity::toDomain)
            .or(Optional::empty);
    }

    @Transactional(readOnly = true)
    public List<Hub> findHubsByIds(List<String> hubIds) {
        return hubReadOnlyRepository.findByHubIdsAndIsDeletedFalse(hubIds)
            .stream()
            .map(HubEntity::toDomain)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Hub> findByHubCenter(String hubCenter) {
        return hubReadOnlyRepository.findByHubCenterIsDeletedFalse(hubCenter)
            .map(HubEntity::toDomain)
            .or(Optional::empty);
    }

    @Override
    public Optional<Hub> findByHubIdWithHubRoutes(String hubId) {
        return hubReadOnlyRepository.findByHubIdAndIsDeletedFalseWithHubRoutes(hubId)
            .map(HubEntity::toDomain)
            .or(Optional::empty);
    }

    @Transactional(readOnly = true)
    public Page<HubEntity> findAllHubsWithKeyword(String keyword, Pageable pageable) {
        return hubReadOnlyRepository.findAllHubsAndIsDeletedFalseWithKeyword(keyword, pageable);
    }

    @Override
    public Hub saveHub(HubForCreate hubForCreate, BigDecimal axisX, BigDecimal axisY) {
        return hubRepository.save(HubEntity.toEntity(hubForCreate, axisX, axisY)).toDomain();
    }

    @Transactional
    @Override
    public Hub updateHub(HubForUpdate hubForUpdate, BigDecimal axisX, BigDecimal axisY) {

        HubEntity hubEntity = hubReadOnlyRepository.findByHubIdAndIsDeletedFalse(hubForUpdate.hubId()).get();

        hubEntity.updateHub(hubForUpdate, axisX, axisY);
        if (hubForUpdate.status() != null) {
            hubRouteRepository.updateStatusByHubId(hubForUpdate.hubId(),
                hubForUpdate.status() == HubStatus.ACTIVE ? HubRouteStatus.ACTIVE : HubRouteStatus.INACTIVE);
        }

        return hubEntity.toDomain();
    }

    @Transactional
    @Override
    public Hub deleteHub(HubForDelete hubForDelete) {

        HubEntity hubEntity = hubReadOnlyRepository.findByHubIdAndIsDeletedFalseWithHubRoutes(hubForDelete.hubId()).get();
        List<HubRouteEntity> hubRouteEntityList = hubRouteReadOnlyRepository.findAllHubRoutesByHubIdAndIsDeletedFalse(hubForDelete.hubId());

        hubEntity.deleteHub(hubForDelete.userId());
        hubRouteEntityList.forEach(hubRoute -> hubRoute.delete(hubForDelete.userId()));

        return hubEntity.toDomain();
    }
}
