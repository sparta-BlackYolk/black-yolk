package com.sparta.blackyolk.logistic_service.hubroute.framework.web.controller;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForDelete;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.application.usecase.HubRouteUseCase;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteCreateRequest;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteCreateResponse;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteDeleteResponse;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteUpdateRequest;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteUpdateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hubs/{hubId}/hub-routes")
public class HubRouteCommandController {

    private final HubRouteUseCase hubRouteUseCase;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public HubRouteCreateResponse createHubRoute(
        @RequestHeader(value = "X-User-Id", required = true) String userId,
        @RequestHeader(value = "X-Role", required = true) String role,
        @PathVariable(value = "hubId") String hubId,
        @Valid @RequestBody HubRouteCreateRequest hubRouteCreateRequest
    ) {
        HubRouteForCreate hubRouteForCreate = HubRouteCreateRequest.toDomain(
            userId,
            role,
            hubId,
            hubRouteCreateRequest
        );

        return hubRouteUseCase.createHubRoute(hubRouteForCreate);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{hubRouteId}")
    public HubRouteUpdateResponse updateHubRoute(
        @RequestHeader(value = "X-User-Id", required = true) String userId,
        @RequestHeader(value = "X-Role", required = true) String role,
        @PathVariable(value = "hubId") String hubId,
        @PathVariable(value = "hubRouteId") String hubRouteId,
        @Valid @RequestBody HubRouteUpdateRequest hubRouteUpdateRequest
    ) {
        HubRouteForUpdate hubRouteForUpdate = HubRouteUpdateRequest.toDomain(
            userId,
            role,
            hubId,
            hubRouteId,
            hubRouteUpdateRequest
        );

        return hubRouteUseCase.updateHubRoute(hubRouteForUpdate);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{hubRouteId}")
    public HubRouteDeleteResponse deleteResponse(
        @RequestHeader(value = "X-User-Id", required = true) String userId,
        @RequestHeader(value = "X-Role", required = true) String role,
        @PathVariable(value = "hubId") String hubId,
        @PathVariable(value = "hubRouteId") String hubRouteId
    ) {
        HubRouteForDelete hubRouteForDelete = new HubRouteForDelete(
            userId,
            role,
            hubId,
            hubRouteId
        );
        HubRoute hubRoute = hubRouteUseCase.deleteHubRoute(hubRouteForDelete);

        return HubRouteDeleteResponse.toDomain(hubRoute);
    }

}
