package com.sparta.blackyolk.logistic_service.hubroute.framework.web.controller;

import com.sparta.blackyolk.logistic_service.hubroute.application.usecase.HubRoutePathUseCase;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRoutePathResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub-routes")
public class HubRoutePathQueryController {

    private final HubRoutePathUseCase hubRoutePathUseCase;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/shortest-path")
    public HubRoutePathResponse getShortestPath(
        @RequestParam(value = "departure") String departure,
        @RequestParam(value = "arrival") String arrival

    ) {
        return hubRoutePathUseCase.getShortestPath(departure, arrival);
    }
}