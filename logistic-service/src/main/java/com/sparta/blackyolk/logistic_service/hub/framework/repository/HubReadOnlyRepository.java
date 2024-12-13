package com.sparta.blackyolk.logistic_service.hub.framework.repository;

import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubReadOnlyRepository {

    Optional<HubEntity> findByHubIdAndIsDeletedFalse(String hubId);
    List<HubEntity> findByHubIdsAndIsDeletedFalse(List<String> hubIds);
    Page<HubEntity> findAllHubsAndIsDeletedFalseWithKeyword(String keyword, Pageable pageable);
    Optional<HubEntity> findByHubCenterIsDeletedFalse(String hubCenter);
}
