package com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;
import java.util.List;
import org.springframework.data.domain.Page;

public record HubRoutePageResponse(
    PageInfo page,
    List<HubRouteResponse> hubRoutes
) {

    public HubRoutePageResponse(
        Page<HubRoute> hubRoutePage
    ) {
        this(
            new PageInfo(
                hubRoutePage.getNumber(),
                hubRoutePage.getSize(),
                hubRoutePage.getTotalElements(),
                hubRoutePage.getTotalPages()
            ),
            HubRouteResponse.toDTO(hubRoutePage.getContent())
        );
    }

    public record HubRouteResponse(
        String hubRouteId,
        String departureHubName,
        String arrivalHubName,
        HubRouteStatus hubRouteStatus,
        Long distance,
        Long duration
    ) {
        public static List<HubRouteResponse> toDTO(List<HubRoute> hubRouteEntityList) {
            return hubRouteEntityList.stream()
                .map(HubRouteResponse::toDTO)
                .toList();
        }

        public static HubRouteResponse toDTO(HubRoute hubRoute) {
            return new HubRouteResponse(
                hubRoute.getHubRouteId(),
                hubRoute.getDepartureHub().getHubName(),
                hubRoute.getArrivalHub().getHubName(),
                hubRoute.getStatus(),
                hubRoute.getDistance(),
                hubRoute.getDuration()
            );
        }
    }

    public record PageInfo(
        int current,
        int size,
        long totalElements,
        int totalPages
    ) {

    }
}
