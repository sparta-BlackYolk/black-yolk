package com.sparta.blackyolk.logistic_service.hubroute.application.service;

import com.sparta.blackyolk.logistic_service.common.exception.CustomException;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForDelete;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRoutePersistencePort;
import com.sparta.blackyolk.logistic_service.hubroute.framework.adapter.HubRoutePersistenceAdapter;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteCreateResponse;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteGetResponse;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRoutePageResponse;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteUpdateResponse;
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
public class HubRouteCacheService {

    private final HubRoutePersistenceAdapter hubRoutePersistenceAdapter;
    private final HubRoutePersistencePort hubRoutePersistencePort;

    @Cacheable(cacheNames = "hub_route_cache", key = "#hubRoute.hubRouteId")
    public HubRouteGetResponse getHubRoute(HubRoute hubRoute) {
        log.info("HubRoute ID for caching: {}", hubRoute.getHubRouteId());
        return HubRouteGetResponse.toDTO(hubRoute);
    }

    @Cacheable(
        cacheNames = "hub_route_page_cache",
        key = "'keyword:' + #keyword + ':page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize + ':sort:' + #pageable.sort.toString()"
    )
    public HubRoutePageResponse getHubRoutes(
        String hubId,
        Pageable pageable,
        String keyword
    ) {

        log.info("[HubRoute search 조회 pageable] : {}", pageable);

        Page<HubRoute> hubRoutePage = hubRoutePersistenceAdapter.findAllHubRoutesByHubIdWithKeyword(
            hubId,
            keyword,
            pageable
        );

        return new HubRoutePageResponse(hubRoutePage);
    }

    @CacheEvict(cacheNames = {"hub_route_page_cache", "hub_route_path_cache"}, allEntries = true)
    public HubRouteCreateResponse createHubRoute(String userId, HubRoute hubRoute) {

        log.info("[HubRoute 생성]: {}", hubRoute);

        HubRoute hubroute = hubRoutePersistencePort.createHubRoute(userId, hubRoute);

        log.info("[HubRoute 저장]: {}", hubRoute.getHubRouteId());

        return HubRouteCreateResponse.toDTO(hubroute);
    }

    @CacheEvict(cacheNames = {"hub_route_cache", "hub_route_page_cache", "hub_route_path_cache"}, allEntries = true)
    public HubRouteUpdateResponse updateHubRoute(HubRouteForUpdate hubRouteForUpdate) {
        HubRoute hubroute = hubRoutePersistencePort.updateHubRoute(hubRouteForUpdate);
        return HubRouteUpdateResponse.toDTO(hubroute);
    }

    @CacheEvict(cacheNames = {"hub_route_cache", "hub_route_page_cache", "hub_route_path_cache"}, allEntries = true)
    public HubRoute deleteHubRoute(HubRouteForDelete hubRouteForDelete) {
        return hubRoutePersistencePort.deleteHubRoute(hubRouteForDelete);
    }

    @Cacheable(cacheNames = "hub_route_cache", key = "'valid:' + #hubRouteId")
    public HubRoute validateHubRoute(String hubRouteId) {
        return hubRoutePersistencePort.findByHubRouteId(hubRouteId).orElseThrow(
            () -> new CustomException(ErrorCode.HUB_ROUTE_NOT_EXIST)
        );
    }
}
