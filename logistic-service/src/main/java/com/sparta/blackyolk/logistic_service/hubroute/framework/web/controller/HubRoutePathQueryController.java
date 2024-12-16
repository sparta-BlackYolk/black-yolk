package com.sparta.blackyolk.logistic_service.hubroute.framework.web.controller;

import com.sparta.blackyolk.logistic_service.hubroute.application.usecase.HubRoutePathUseCase;
import com.sparta.blackyolk.logistic_service.hubroute.application.util.TimeSlotWeightMapper;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRoutePathResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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
    private final TimeSlotWeightMapper timeSlotWeightMapper;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/shortest-path")
    public HubRoutePathResponse getShortestPath(
        @RequestHeader(value = "X-User-Id", required = true) String userId,
        @RequestHeader(value = "X-Role", required = true) String role,
        @RequestParam(value = "departure") String departure,
        @RequestParam(value = "arrival") String arrival

    ) {
        hubRoutePathUseCase.validateMaster(role);

        LocalDateTime now = LocalDateTime.now();
        String currentTimeSlot = timeSlotWeightMapper.getCurrentTimeSlot(now);

        return hubRoutePathUseCase.getShortestPath(departure, arrival, currentTimeSlot);
    }
}
