package com.sparta.blackyolk.logistic_service.hubroute.application.service;

import com.sparta.blackyolk.logistic_service.common.exception.CustomException;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.port.HubPersistencePort;
import com.sparta.blackyolk.logistic_service.hub.application.service.HubService;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForDelete;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForRead;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRouteCalculator;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRoutePersistencePort;
import com.sparta.blackyolk.logistic_service.hubroute.application.usecase.HubRouteUseCase;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubRouteService implements HubRouteUseCase {

    private final HubRoutePersistencePort hubRoutePersistencePort;
    private final HubPersistencePort hubPersistencePort;
    private final HubService hubService;
    private final HubRouteCalculator hubRouteCalculator;

    // TODO : 쿼리 몇번 날아가는 지 확인, 불필요한 쿼리가 날아가지 않은가?

    @Override
    public HubRoute getHubRoute(HubRouteForRead hubRouteForRead) {

        HubRoute hubRoute = validateHubRoute(hubRouteForRead.hubRouteId());
        validateDepartureHubInRoute(hubRoute, hubRouteForRead.departureHubId());

        return hubRoute;
    }

    @Override
    public HubRoute createHubRoute(HubRouteForCreate hubRouteForCreate) {

        validateMaster(hubRouteForCreate.role());

        List<Hub> hubs = hubPersistencePort.findHubsByIds(
            List.of(hubRouteForCreate.departureHubId(), hubRouteForCreate.arrivalHubId())
        );

        Hub departureHub = hubs.stream()
            .filter(hub -> hub.getHubId().equals(hubRouteForCreate.departureHubId()))
            .findFirst()
            .orElseThrow(
                () -> new CustomException(ErrorCode.HUB_NOT_EXIST)
            );

        Hub arrivalHub = hubs.stream()
            .filter(hub -> hub.getHubId().equals(hubRouteForCreate.arrivalHubId()))
            .findFirst()
            .orElseThrow(
                () -> new CustomException(ErrorCode.HUB_NOT_EXIST)
            );

        HubRoute hubRoute = new HubRoute(
            departureHub,
            arrivalHub,
            hubRouteForCreate.timeSlot(),
            hubRouteForCreate.timeSlotWeight()
        );

        return hubRoutePersistencePort.createHubRoute(hubRouteForCreate.userId(), hubRoute);
    }

    @Override
    public HubRoute updateHubRoute(HubRouteForUpdate hubRouteForUpdate) {

        validateMaster(hubRouteForUpdate.role());
        HubRoute hubRoute = validateHubRoute(hubRouteForUpdate.hubRouteId());
        validateDepartureHubInRoute(hubRoute, hubRouteForUpdate.departureHubId());

        Hub arrivalHub = Optional.ofNullable(hubRouteForUpdate.arrivalHubId())
            .map(hubService::validateHub)
            .orElse(null);

        // TODO : 실제 로직으로 대체하기
//        BigDecimal distance = arrivalHub != null
//            ? hubRouteCalculator.getDistance(hubRoute.getDepartureHub(), arrivalHub)
//            : null;
//
//        Integer duration = distance != null
//            ? hubRouteCalculator.getDuration(distance)
//            : null;
        BigDecimal distance = arrivalHub != null
            ? new BigDecimal("20.5")
            : null;

        Integer duration = distance != null
            ? 30
            : null;

        return hubRoutePersistencePort.updateHubRoute(hubRouteForUpdate, arrivalHub, distance, duration);
    }

    @Override
    public HubRoute deleteHubRoute(HubRouteForDelete hubRouteForDelete) {

        validateMaster(hubRouteForDelete.role());
        HubRoute hubRoute = validateHubRoute(hubRouteForDelete.hubRouteId());
        validateDepartureHubInRoute(hubRoute, hubRouteForDelete.departureHubId());

        return hubRoutePersistencePort.deleteHubRoute(hubRouteForDelete);
    }

    public HubRoute validateHubRoute(String hubRouteId) {
        return hubRoutePersistencePort.findByHubRouteId(hubRouteId).orElseThrow(
            () -> new CustomException(ErrorCode.HUB_ROUTE_NOT_EXIST)
        );
    }

    private void validateMaster(String role) {
        if (!"MASTER".equals(role)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    private void validateDepartureHubInRoute(HubRoute hubRoute, String departureHubId) {
        if (!hubRoute.isDepartureHubBelongToHubRoute(departureHubId)) {
            throw new CustomException(ErrorCode.HUB_ROUTE_BAD_REQUEST);
        }
    }
}
