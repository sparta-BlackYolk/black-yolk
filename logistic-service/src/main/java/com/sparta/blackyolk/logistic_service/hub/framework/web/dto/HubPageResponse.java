package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import com.sparta.blackyolk.logistic_service.hub.data.vo.HubStatus;
import java.util.List;
import org.springframework.data.domain.Page;

public record HubPageResponse(
    PageInfo page,
    List<HubResponse> hubs
) {

    public HubPageResponse(
        Page<HubEntity> hubPage
    ) {
        this(
            new PageInfo(
                hubPage.getNumber(),
                hubPage.getSize(),
                hubPage.getTotalElements(),
                hubPage.getTotalPages()
            ),
            HubResponse.toDTO(hubPage.getContent())
        );
    }

    public record HubResponse(
        String id,
        Long hubManagerId,
        String name,
        String center,
        HubStatus status,
        HubCoordinateResponse coordinate,
        HubAddressResponse address
    ) {
        public static List<HubResponse> toDTO(List<HubEntity> hubEntityList) {
            return hubEntityList.stream()
                .map(HubResponse::toDTO)
                .toList();
        }

        public static HubResponse toDTO(HubEntity hubEntity) {
            return new HubResponse(
                hubEntity.getHubId(),
                hubEntity.getHubManagerId(),
                hubEntity.getHubName(),
                hubEntity.getHubCenter(),
                hubEntity.getStatus(),
                HubCoordinateResponse.fromDomain(hubEntity.getHubCoordinate().toDomain()),
                HubAddressResponse.fromDomain(hubEntity.getHubAddress().toDomain())
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
