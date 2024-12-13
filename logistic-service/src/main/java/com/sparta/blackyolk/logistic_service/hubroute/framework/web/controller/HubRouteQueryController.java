package com.sparta.blackyolk.logistic_service.hubroute.framework.web.controller;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForRead;
import com.sparta.blackyolk.logistic_service.hubroute.application.usecase.HubRouteUseCase;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteGetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hubs/{hubId}/hub-routes")
public class HubRouteQueryController {

    private final HubRouteUseCase hubRouteUseCase;

    // TODO : 지우기
    private final Long TEST_USER = 1L;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{hubRouteId}")
    public HubRouteGetResponse getHub(
        @PathVariable(value = "hubId") String hubId,
        @PathVariable(value = "hubRouteId") String hubRouteId
    ) {
        // TODO : user token, user role 받기
        HubRouteForRead hubRouteForRead = new HubRouteForRead(
            TEST_USER,
            hubId,
            hubRouteId
        );
        HubRoute hubRoute = hubRouteUseCase.getHubRoute(hubRouteForRead);

        return HubRouteGetResponse.toDTO(hubRoute);
    }

}
