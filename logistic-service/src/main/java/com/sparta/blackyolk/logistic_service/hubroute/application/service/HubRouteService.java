package com.sparta.blackyolk.logistic_service.hubroute.application.service;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.service.HubService;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForDelete;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRouteCalculator;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRoutePersistencePort;
import com.sparta.blackyolk.logistic_service.hubroute.application.usecase.HubRouteUseCase;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubRouteService implements HubRouteUseCase {

    private final HubRoutePersistencePort hubRoutePersistencePort;
    private final HubService hubService;
    private final HubRouteCalculator hubRouteCalculator;

    // TODO : 쿼리 몇번 날아가는 지 확인, 불필요한 쿼리가 날아가지 않은가?

    @Override
    public HubRoute createHubRoute(HubRouteForCreate hubRouteForCreate) {

        // TODO : 사용자 권한 확인

        Hub departureHub = hubService.validateHub(hubRouteForCreate.departureHubId());
        Hub arrivalHub = hubService.validateHub(hubRouteForCreate.arrivalHubId());

        // TODO : service 로직 말고 더 좋은 곳에 있을 수 있는가?
        BigDecimal distance = hubRouteCalculator.getDistance(departureHub, arrivalHub);
        Integer duration = hubRouteCalculator.getDuration(distance);

        // TODO : 예외처리 추가하기
        return hubRoutePersistencePort.createHubRoute(hubRouteForCreate, distance, duration);
    }

    @Override
    public HubRoute updateHubRoute(HubRouteForUpdate hubRouteForUpdate) {

        // TODO : 사용자 권한 확인

        HubRoute hubRoute = validateHubRoute(hubRouteForUpdate.hubRouteId());

        // TODO : 예외처리 하기
        if (!hubRoute.isDepartureHubBelongToHubRoute(hubRouteForUpdate.departureHubId())) {

        }

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

        // TODO : 사용자 권한 확인
        HubRoute hubRoute = validateHubRoute(hubRouteForDelete.hubRouteId());

        // TODO : 예외처리 하기
        if (!hubRoute.isDepartureHubBelongToHubRoute(hubRouteForDelete.departureHubId())) {

        }

        return hubRoutePersistencePort.deleteHubRoute(hubRouteForDelete);
    }

    public HubRoute validateHubRoute(String hubRouteId) {
        // TODO : 예외처리하기
        return hubRoutePersistencePort.findByHubRouteId(hubRouteId).orElseThrow(

        );
    }
}
