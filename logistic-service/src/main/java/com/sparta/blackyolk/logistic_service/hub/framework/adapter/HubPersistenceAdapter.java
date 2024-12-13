package com.sparta.blackyolk.logistic_service.hub.framework.adapter;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForDelete;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForUpdate;
import com.sparta.blackyolk.logistic_service.hub.application.port.HubPersistencePort;
import com.sparta.blackyolk.logistic_service.hub.framework.repository.HubReadOnlyRepository;
import com.sparta.blackyolk.logistic_service.hub.framework.repository.HubRepository;
import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
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
    private final HubReadOnlyRepository hubReadOnlyRepository;

    public Optional<Hub> findByHubId(String hubId) {
        return hubReadOnlyRepository.findByHubIdAndIsDeletedFalse(hubId)
            .map(HubEntity::toDomain)
            .or(Optional::empty);
    }

    public List<Hub> findHubsByIds(List<String> hubIds) {
        return hubReadOnlyRepository.findByHubIdsAndIsDeletedFalse(hubIds)
            .stream()
            .map(HubEntity::toDomain)
            .collect(Collectors.toList());
    }

    public Page<HubEntity> findAllHubs(String keyword, Pageable pageable) {
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

        return hubEntity.toDomain();
    }

    @Transactional
    @Override
    public Hub deleteHub(HubForDelete hubForDelete) {

        HubEntity hubEntity = hubReadOnlyRepository.findByHubIdAndIsDeletedFalse(hubForDelete.hubId()).get();
        hubEntity.deleteHub(hubForDelete);

        return hubEntity.toDomain();
    }
}
