package com.sparta.blackyolk.logistic_service.hub.framework.web.controller;

import com.sparta.blackyolk.logistic_service.common.pagenation.PaginationConstraint;
import com.sparta.blackyolk.logistic_service.hub.application.service.HubCacheService;
import com.sparta.blackyolk.logistic_service.hub.framework.adapter.HubPersistenceAdapter;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubGetResponse;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hubs")
public class HubQueryController {

    private final HubCacheService hubCacheService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{hubId}")
    public HubGetResponse getHub(
        @PathVariable(value = "hubId") String hubId
    ) {
        // TODO : user token, user role 받아야 하나?
        return hubCacheService.getHub(hubId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @PaginationConstraint(defaultSort = "createdAt", defaultDirection = "DESC", availableSize = {10,30,50}, defaultSize = 10)
    public HubPageResponse getHubs(
        Pageable pageable,
        @RequestParam(required = false) String keyword
    ) {
        // TODO : user token, user role 받아야 하나?
        log.info("[Hub search 조회 pageable] : {}", pageable);

        return hubCacheService.getHubs(pageable, keyword);
    }
}
