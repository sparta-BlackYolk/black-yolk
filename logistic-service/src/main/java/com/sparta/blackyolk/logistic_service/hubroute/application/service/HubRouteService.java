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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

        Hub departureHub = hubService.validateHub(hubRouteForRead.departureHubId());
        HubRoute hubRoute = validateHubRoute(hubRouteForRead.hubRouteId());
        validateDepartureHubInRoute(hubRoute, departureHub);

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

        validateArrivalHubCenter(arrivalHub, hubRouteForCreate.targetHubCenter());
        validateConnection(departureHub, arrivalHub);

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
        Hub departureHub = hubService.validateHub(hubRouteForUpdate.departureHubId());
        HubRoute hubRoute = validateHubRoute(hubRouteForUpdate.hubRouteId());
        validateDepartureHubInRoute(hubRoute, departureHub);

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
        Hub departureHub = hubService.validateHub(hubRouteForDelete.departureHubId());
        HubRoute hubRoute = validateHubRoute(hubRouteForDelete.hubRouteId());
        validateDepartureHubInRoute(hubRoute, departureHub);

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

    private void validateArrivalHubCenter(Hub arrivalHub, String arrivalHubCenter) {
        if (!arrivalHub.getHubCenter().equals(arrivalHubCenter)) {
            throw new CustomException(ErrorCode.HUB_ROUTE_BAD_REQUEST);
        }
    }

    private void validateDepartureHubInRoute(HubRoute hubRoute, Hub departureHub) {
        if (!hubRoute.isDepartureHubBelongToHubRoute(departureHub.getHubId())) {
            throw new CustomException(ErrorCode.HUB_ROUTE_BAD_REQUEST);
        }
    }

    private static final Map<String, Set<String>> HUB_CONNECTIONS = Map.of(
        "경기 남부 센터", Set.of("경기 북부 센터", "서울특별시 센터", "인천광역시 센터", "강원특별자치도 센터", "경상북도 센터", "대전광역시 센터", "대구광역시 센터"),
        "대전광역시 센터", Set.of("충청남도 센터", "충청북도 센터", "세종특별자치시 센터", "전북특별자치도 센터", "광주광역시 센터", "전라남도 센터", "경기 남부 센터", "대구광역시 센터"),
        "대구광역시 센터", Set.of("경상북도 센터", "경상남도 센터", "부산광역시 센터", "울산광역시 센터", "경기 남부 센터", "대전광역시 센터"),
        "경상북도 센터", Set.of("경기 남부 센터", "대구광역시 센터")
    );

    private boolean isConnected(String hubCenter1, String hubCenter2) {
        // 양방향 연결 확인
        return HUB_CONNECTIONS.getOrDefault(hubCenter1, Set.of()).contains(hubCenter2) ||
               HUB_CONNECTIONS.getOrDefault(hubCenter2, Set.of()).contains(hubCenter1);
    }

    private void validateConnection(Hub departureHub, Hub arrivalHub) {
        if (!isConnected(departureHub.getHubCenter(), arrivalHub.getHubCenter())) {
            throw new CustomException(ErrorCode.HUB_ROUTE_BAD_REQUEST);
        }
    }
}
