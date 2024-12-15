package com.sparta.blackyolk.logistic_service.hub.framework.web.controller;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForDelete;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForUpdate;
import com.sparta.blackyolk.logistic_service.hub.application.usecase.HubUseCase;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubCreateRequest;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubCreateResponse;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubDeleteResponse;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubUpdateRequest;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubUpdateResponse;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hubs")
public class HubCommandController {

    private final HubUseCase hubUseCase;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public HubCreateResponse createHub(
        @Valid @RequestBody HubCreateRequest hubCreateRequest,
        @RequestHeader(value = "X-User-Id", required = true) String userId,
        @RequestHeader(value = "X-Role", required = true) String role
    ) {
        log.info("[Hub 생성] header 확인: {}", userId);
        log.info("[Hub 생성] header 확인: {}", role);

        HubForCreate hubForCreate = HubCreateRequest.toDomain(
            userId,
            role,
            hubCreateRequest
        );

        return hubUseCase.createHub(hubForCreate);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{hubId}")
    public HubUpdateResponse updateHub(
        @PathVariable(value = "hubId") String hubId,
        @Valid @RequestBody HubUpdateRequest hubUpdateRequest,
        @RequestHeader(value = "X-User-Id", required = true) String userId,
        @RequestHeader(value = "X-Role", required = true) String role
    ) {
        HubForUpdate hubForUpdate = HubUpdateRequest.toDomain(
            userId,
            role,
            hubId,
            hubUpdateRequest
        );

        return hubUseCase.updateHub(hubForUpdate);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{hubId}")
    public HubDeleteResponse deleteHub(
        @PathVariable(value = "hubId") String hubId,
        @RequestHeader(value = "X-User-Id", required = true) String userId,
        @RequestHeader(value = "X-Role", required = true) String role
    ) {
        HubForDelete hubForDelete = new HubForDelete(
            userId,
            role,
            hubId
        );
        Hub hub = hubUseCase.deleteHub(hubForDelete);

        return HubDeleteResponse.toDTO(hub);
    }
}
