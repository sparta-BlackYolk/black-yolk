package com.sparta.blackyolk.logistic_service.hubroute.framework.web.controller;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.application.usecase.HubRouteUseCase;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteCreateRequest;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteCreateResponse;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteUpdateRequest;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hubs/{hubId}/hub-routes")
public class HubRouteCommandController {

    private final HubRouteUseCase hubRouteUseCase;

    // TODO : 지우기
    private final Long TEST_USER = 1L;

    // TODO : validated 어노테이션 사용해보기

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public HubRouteCreateResponse createHubRoute(
        @PathVariable(value = "hubId") String hubId,
        @Valid @RequestBody HubRouteCreateRequest hubRouteCreateRequest
    ) {
        // TODO : user token, user role 받기
        HubRouteForCreate hubRouteForCreate = HubRouteCreateRequest.toDomain(
            TEST_USER,
            hubId,
            hubRouteCreateRequest
        );
        HubRoute hubRoute = hubRouteUseCase.createHubRoute(hubRouteForCreate);

        return HubRouteCreateResponse.toDTO(hubRoute);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{hubRouteId}")
    public HubRouteUpdateResponse updateHubRoute(
        @PathVariable(value = "hubId") String hubId,
        @PathVariable(value = "hubRouteId") String hubRouteId,
        @Valid @RequestBody HubRouteUpdateRequest hubRouteUpdateRequest
    ) {
        // TODO : user token, user role 받기
        HubRouteForUpdate hubRouteForUpdate = HubRouteUpdateRequest.toDomain(
            TEST_USER,
            hubId,
            hubRouteId,
            hubRouteUpdateRequest
        );
        HubRoute hubRoute = hubRouteUseCase.updateHubRoute(hubRouteForUpdate);

        return HubRouteUpdateResponse.toDTO(hubRoute);
    }

}
