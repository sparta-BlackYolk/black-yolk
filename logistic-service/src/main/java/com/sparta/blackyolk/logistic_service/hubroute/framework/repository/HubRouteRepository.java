package com.sparta.blackyolk.logistic_service.hubroute.framework.repository;

import com.sparta.blackyolk.logistic_service.hubroute.data.HubRouteEntity;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HubRouteRepository extends JpaRepository<HubRouteEntity, String> {

    @Modifying
    @Query("update HubRouteEntity hr "
        + "set hr.status = :status "
        + "where hr.departureHub.hubId = :hubId "
        + "or hr.arrivalHub.hubId = :hubId")
    void updateStatusByHubId(@Param("hubId") String hubId, @Param("status")HubRouteStatus hubRouteStatus);
}
