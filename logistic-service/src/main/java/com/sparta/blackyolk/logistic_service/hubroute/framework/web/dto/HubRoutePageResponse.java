package com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hubroute.data.HubRouteEntity;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;

public record HubRoutePageResponse(
    PageInfo page,
    List<HubRouteResponse> hubRoutes
) {

    public HubRoutePageResponse(
        Page<HubRouteEntity> hubRoutePage
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
        BigDecimal distance,
        Integer duration
    ) {
        public static List<HubRouteResponse> toDTO(List<HubRouteEntity> hubRouteEntityList) {
            return hubRouteEntityList.stream()
                .map(HubRouteResponse::toDTO)
                .toList();
        }

        public static HubRouteResponse toDTO(HubRouteEntity hubRouteEntity) {
            return new HubRouteResponse(
                hubRouteEntity.getHubRouteId(),
                hubRouteEntity.getDepartureHub().getHubName(),
                hubRouteEntity.getArrivalHub().getHubName(),
                hubRouteEntity.getStatus(),
                hubRouteEntity.getDistance(),
                hubRouteEntity.getDuration()
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
