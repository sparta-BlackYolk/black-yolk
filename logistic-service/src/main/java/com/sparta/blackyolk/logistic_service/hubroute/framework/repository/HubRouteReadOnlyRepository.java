package com.sparta.blackyolk.logistic_service.hubroute.framework.repository;

import com.sparta.blackyolk.logistic_service.hubroute.data.HubRouteEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubRouteReadOnlyRepository {
    Optional<HubRouteEntity> findByHubRouteIdAndIsDeletedFalse(String hubRouteId);
    Page<HubRouteEntity> findAllHubRoutesByHubIdAndIsDeletedFalseWithKeyword(String hubId, String keyword, Pageable pageable);
    List<HubRouteEntity> findAllHubRoutesByHubIdAndIsDeletedFalse(String hubId);
    Optional<HubRouteEntity> findByDepartureHubIdAndArrivalHubId(String departureHubId, String arrivalHubId);
    List<HubRouteEntity> findAllHubRoutesAndIsDeletedFalseAndActive();
    List<HubRouteEntity> findAllByHubIdAndIsDeletedFalseAndActive(String hubId);
}
