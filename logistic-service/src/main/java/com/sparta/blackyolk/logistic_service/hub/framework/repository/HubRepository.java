package com.sparta.blackyolk.logistic_service.hub.framework.repository;

import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HubRepository extends JpaRepository<HubEntity, String> {

    @Query("select h from HubEntity h where h.hubId = :hubId and h.isDeleted = false")
    Optional<HubEntity> findByHubIdAndIsDeletedFalse(@Param("hubId") String hubId);

    @Query("select distinct h from HubEntity h where h.hubId in :hubIds and h.isDeleted = false")
    List<HubEntity> findByHubIdsAndIsDeletedFalse(List<String> hubIds);
}
