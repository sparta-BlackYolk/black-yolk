package com.sparta.blackyolk.logistic_service.hubroute.application.service;

import com.sparta.blackyolk.logistic_service.common.exception.CustomException;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.port.HubPersistencePort;
import com.sparta.blackyolk.logistic_service.hub.application.service.HubCacheService;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.DriveInfo;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForDelete;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForRead;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.application.usecase.HubRouteUseCase;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteCreateResponse;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteGetResponse;
import com.sparta.blackyolk.logistic_service.hubroute.framework.web.dto.HubRouteUpdateResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubRouteService implements HubRouteUseCase {

    private final HubPersistencePort hubPersistencePort;
    private final HubCacheService hubCacheService;
    private final HubRouteCacheService hubRouteCacheService;
    private final DriveService driveService;

    // TODO : 쿼리 몇번 날아가는 지 확인, 불필요한 쿼리가 날아가지 않은가?

    @Override
    public HubRouteGetResponse getHubRoute(HubRouteForRead hubRouteForRead) {

        Hub departureHub = hubCacheService.validateHub(hubRouteForRead.departureHubId());
        HubRoute hubRoute = hubRouteCacheService.validateHubRoute(hubRouteForRead.hubRouteId());
        validateDepartureHubInRoute(hubRoute, departureHub);

        return hubRouteCacheService.getHubRoute(hubRoute);
    }

    @Override
    public HubRouteCreateResponse createHubRoute(HubRouteForCreate hubRouteForCreate) {

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

        DriveInfo driveInfo = driveService.getDriveInfo(departureHub, arrivalHub);

        HubRoute hubRoute = new HubRoute(
            departureHub,
            arrivalHub,
            driveInfo.distance(),
            driveInfo.duration()
        );

        return hubRouteCacheService.createHubRoute(hubRouteForCreate.userId(), hubRoute);
    }

    @Override
    public HubRouteUpdateResponse updateHubRoute(HubRouteForUpdate hubRouteForUpdate) {

        validateMaster(hubRouteForUpdate.role());
        Hub departureHub = hubCacheService.validateHub(hubRouteForUpdate.departureHubId());
        HubRoute hubRoute = hubRouteCacheService.validateHubRoute(hubRouteForUpdate.hubRouteId());
        validateDepartureHubInRoute(hubRoute, departureHub);

        return hubRouteCacheService.updateHubRoute(hubRouteForUpdate);
    }

    @Override
    public HubRoute deleteHubRoute(HubRouteForDelete hubRouteForDelete) {

        validateMaster(hubRouteForDelete.role());
        Hub departureHub = hubCacheService.validateHub(hubRouteForDelete.departureHubId());
        HubRoute hubRoute = hubRouteCacheService.validateHubRoute(hubRouteForDelete.hubRouteId());
        validateDepartureHubInRoute(hubRoute, departureHub);

        return hubRouteCacheService.deleteHubRoute(hubRouteForDelete);
    }

    private void validateMaster(String role) {
        if (!"MASTER".equals(role)) {
            throw new CustomException(ErrorCode.USER_ACCESS_DENIED);
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
