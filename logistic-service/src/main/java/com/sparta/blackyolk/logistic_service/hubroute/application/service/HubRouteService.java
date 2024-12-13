package com.sparta.blackyolk.logistic_service.hubroute.application.service;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.service.HubService;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRouteCalculator;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRoutePersistencePort;
import com.sparta.blackyolk.logistic_service.hubroute.application.usecase.HubRouteUseCase;
import java.math.BigDecimal;
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

        // TODO : 허브 간 이동 외부 API 연결 -> Impl 코드 완성하기, 궁금한 거 - 도메인 이나 도메인 서비스로 따로 분리할 수 있을까?
        BigDecimal distance = hubRouteCalculator.getDistance(
            departureHub,
            arrivalHub
        );
        Integer duration = hubRouteCalculator.getDuration(distance);

        HubRoute hubRoute = new HubRoute(
            departureHub,
            arrivalHub,
            hubRouteForCreate.timeSlot(),
            duration,
            distance,
            hubRouteForCreate.timeSlotWeight()
        );

        // TODO : 예외처리 추가하기
        return hubRoutePersistencePort.createHubRoute(hubRouteForCreate.userId(), hubRoute);
    }
}
