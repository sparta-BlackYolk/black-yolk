package com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import java.math.BigDecimal;
import java.util.List;

public record HubRoutePathResponse(
    String departureHub,
    String arrivalHub,
    BigDecimal totalDistance,
    int totalDuration,
    List<PathResponse> paths
) {
    public record PathResponse(
        String from,
        String to,
        BigDecimal distance,
        int duration
    ) {

    }

    public static HubRoutePathResponse toDTO(
        String departureHub,
        String arrivalHub,
        List<HubRoute> hubRouteList
    ) {
        List<PathResponse> paths = hubRouteList.stream()
            .map(route -> new PathResponse(
                route.getDepartureHub().getHubName(),
                route.getArrivalHub().getHubName(),
                route.getDistance(),
                route.getDuration()
            ))
            .toList();

        BigDecimal totalDistance = hubRouteList.stream()
            .map(HubRoute::getDistance)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalDuration = hubRouteList.stream()
            .mapToInt(HubRoute::getDuration)
            .sum();

        return new HubRoutePathResponse(
            departureHub,
            arrivalHub,
            totalDistance,
            totalDuration,
            paths
        );
    }
}
