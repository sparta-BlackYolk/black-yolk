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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/hubs")
public class HubCommandController {

    private final HubUseCase hubUseCase;

    // TODO : 지우기
    private final Long TEST_USER = 1L;
    private final String TEST_ROLE = "MASTER";

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public HubCreateResponse createHub(
        @Valid @RequestBody HubCreateRequest hubCreateRequest
    ) {
        // TODO : user token, user role 받기
        HubForCreate hubForCreate = HubCreateRequest.toDomain(
            TEST_USER,
            TEST_ROLE,
            hubCreateRequest
        );
        Hub hub = hubUseCase.createHub(hubForCreate);

        return HubCreateResponse.toDTO(hub);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{hubId}")
    public HubUpdateResponse updateHub(
        @PathVariable(value = "hubId") String hubId,
        @RequestBody HubUpdateRequest hubUpdateRequest
    ) {
        // TODO : user token, user role 받기
        HubForUpdate hubForUpdate = HubUpdateRequest.toDomain(
            TEST_USER,
            TEST_ROLE,
            hubId,
            hubUpdateRequest
        );
        Hub hub = hubUseCase.updateHub(hubForUpdate);

        return HubUpdateResponse.toDTO(hub);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{hubId}")
    public HubDeleteResponse deleteHub(
        @PathVariable(value = "hubId") String hubId
    ) {
        // TODO : user token, user role 받기
        HubForDelete hubForDelete = new HubForDelete(
            TEST_USER,
            TEST_ROLE,
            hubId
        );
        Hub hub = hubUseCase.deleteHub(hubForDelete);

        return HubDeleteResponse.toDTO(hub);
    }
}
