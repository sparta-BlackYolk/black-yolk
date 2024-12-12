package com.sparta.blackyolk.logistic_service.hub.framework.repository;

import com.sparta.blackyolk.logistic_service.hub.persistence.HubEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubRepository extends JpaRepository<HubEntity, Long> {

}
