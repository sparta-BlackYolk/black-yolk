package com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import java.util.List;

public record HubRoutePathResponse(
    String departureHub,
    String arrivalHub,
    Long totalDistance,
    double totalDuration,
    List<PathResponse> paths
) {
    public record PathResponse(
        String from,
        String to,
        Long distance,
        double duration
    ) {

    }

    public static HubRoutePathResponse toDTO(
        String departureHub,
        String arrivalHub,
        List<HubRoute> hubRouteList,
        double timeSlotWeight
    ) {
        List<PathResponse> paths = hubRouteList.stream()
            .map(route -> new PathResponse(
                route.getDepartureHub().getHubName(),
                route.getArrivalHub().getHubName(),
                route.getDistance(),
                route.getDuration()*timeSlotWeight
            ))
            .toList();

        Long totalDistance = hubRouteList.stream()
            .map(HubRoute::getDistance) // Long 스트림으로 변환
            .reduce(0L, Long::sum); // 합계 계산

        double totalDuration = hubRouteList.stream()
            .mapToDouble(route -> route.getDuration() * timeSlotWeight)
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
