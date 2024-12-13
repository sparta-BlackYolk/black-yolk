package com.sparta.blackyolk.logistic_service.hubroute.framework.repository;

import com.sparta.blackyolk.logistic_service.hubroute.data.HubRouteEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HubRouteRepository extends JpaRepository<HubRouteEntity, String> {

    @Query("select hr from HubRouteEntity hr "
        + "join fetch hr.departureHub "
        + "join fetch hr.arrivalHub "
        + "where hr.hubRouteId = :hubRouteId and hr.isDeleted = false")
    Optional<HubRouteEntity> findByHubRouteIdAndIsDeletedFalse(@Param("hubRouteId")String hubRouteId);
}
