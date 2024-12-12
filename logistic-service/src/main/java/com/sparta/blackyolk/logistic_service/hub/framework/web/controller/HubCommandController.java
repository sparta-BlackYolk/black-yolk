package com.sparta.blackyolk.logistic_service.hub.framework.web.controller;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.application.usecase.HubUseCase;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubAddressRequest;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubCreateRequest;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
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

    // TODO : validated 어노테이션 사용해보기

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public HubCreateResponse createHub(
        @Valid @RequestBody HubCreateRequest hubCreateRequest
    ) {
        // TODO : user token, user role 받기
        HubForCreate hubForCreate = HubAddressRequest.toDomain(
            TEST_USER,
            hubCreateRequest
        );
        Hub hub = hubUseCase.createHub(hubForCreate);

        return HubCreateResponse.toDTO(hub);
    }
}
