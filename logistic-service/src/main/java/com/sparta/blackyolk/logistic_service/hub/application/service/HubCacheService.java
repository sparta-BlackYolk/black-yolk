package com.sparta.blackyolk.logistic_service.hub.application.service;

import com.sparta.blackyolk.logistic_service.common.exception.CustomException;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForDelete;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForUpdate;
import com.sparta.blackyolk.logistic_service.hub.application.port.HubPersistencePort;
import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import com.sparta.blackyolk.logistic_service.hub.framework.adapter.HubPersistenceAdapter;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubCreateResponse;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubGetResponse;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubPageResponse;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubUpdateResponse;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubCacheService {

    private final HubPersistencePort hubPersistencePort;
    private final HubPersistenceAdapter hubPersistenceAdapter;

    @Cacheable(cacheNames = "hub_cache", key = "#hubId")
    public HubGetResponse getHub(String hubId) {
        return hubPersistencePort.findByHubId(hubId)
            .map(HubGetResponse::toDTO)
            .orElseThrow(
                () -> new CustomException(ErrorCode.HUB_NOT_EXIST)
            );
    }

    @Cacheable(
        cacheNames = "hub_page_cache",
        key = "'keyword:' + #keyword + ':page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize + ':sort:' + #pageable.sort.toString()"
    )
    public HubPageResponse getHubs(
        Pageable pageable,
        String keyword
    ) {

        log.info("[Hub search 조회 pageable] : {}", pageable);

        Page<HubEntity> hubPage = hubPersistenceAdapter.findAllHubsWithKeyword(keyword, pageable);

        return new HubPageResponse(hubPage);
    }

    @CacheEvict(cacheNames = "hub_page_cache", allEntries = true)
    public HubCreateResponse createHub(HubForCreate hubForCreate, BigDecimal axisX, BigDecimal axisY) {

        log.info("Creating hub with data: {}", hubForCreate);

        Hub hub = hubPersistencePort.saveHub(hubForCreate, axisX, axisY);

        log.info("Saved hub: {}", hub.getHubName());

        return HubCreateResponse.toDTO(hub);
    }

    @CacheEvict(cacheNames = {"hub_cache", "hub_page_cache"}, allEntries = true)
    public HubUpdateResponse updateHub(HubForUpdate hubForUpdate, BigDecimal axisX, BigDecimal axisY) {
        Hub hub = hubPersistencePort.updateHub(hubForUpdate, axisX, axisY);
        return HubUpdateResponse.toDTO(hub);
    }

    @CacheEvict(cacheNames = {"hub_cache", "hub_page_cache"}, allEntries = true)
    public Hub deleteHub(HubForDelete hubForDelete) {
        return hubPersistencePort.deleteHub(hubForDelete);
    }

    @Cacheable(cacheNames = "hub_cache", key = "'valid:' +#hubId")
    public Hub validateHub(String hubId) {
        return hubPersistencePort.findByHubId(hubId).orElseThrow(
            () -> new CustomException(ErrorCode.HUB_NOT_EXIST)
        );
    }
}
