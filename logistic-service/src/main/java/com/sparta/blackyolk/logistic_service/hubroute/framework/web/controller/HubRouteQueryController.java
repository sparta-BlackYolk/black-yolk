package com.sparta.blackyolk.logistic_service.hubroute.framework.web.controller;

import com.sparta.blackyolk.logistic_service.common.pagenation.PaginationConstraint;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForRead;
import com.sparta.blackyolk.logistic_service.hubroute.application.service.HubRouteCacheService;
import com.sparta.blackyolk.logistic_service.hubroute.application.usecase.HubRouteUseCase;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteGetResponse;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRoutePageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hubs/{hubId}/hub-routes")
public class HubRouteQueryController {

    private final HubRouteUseCase hubRouteUseCase;
    private final HubRouteCacheService hubRouteCacheService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{hubRouteId}")
    public HubRouteGetResponse getHubRoute(
        @RequestHeader(value = "X-User-Id", required = true) String userId,
        @RequestHeader(value = "X-Role", required = true) String role,
        @PathVariable(value = "hubId") String hubId,
        @PathVariable(value = "hubRouteId") String hubRouteId
    ) {
        HubRouteForRead hubRouteForRead = new HubRouteForRead(
            hubId,
            hubRouteId
        );

        return hubRouteUseCase.getHubRoute(hubRouteForRead);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @PaginationConstraint(defaultSort = "createdAt", defaultDirection = "DESC", availableSize = {10,30,50}, defaultSize = 10)
    public HubRoutePageResponse getHubRoutes(
        @RequestHeader(value = "X-User-Id", required = true) String userId,
        @RequestHeader(value = "X-Role", required = true) String role,
        @PathVariable(value = "hubId") String hubId,
        Pageable pageable,
        @RequestParam(required = false) String keyword
    ) {
        log.info("[HubRoute 목록 조회] userId: {}", userId);

        return hubRouteCacheService.getHubRoutes(hubId, pageable, keyword);
    }

}
