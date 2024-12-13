package com.sparta.blackyolk.logistic_service.hubroute.framework.web.distance;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRouteCalculator;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class HubRouteCalculatorImpl implements HubRouteCalculator {

    // TODO : 허브 간 이동 외부 API 연결 -> Impl 코드 완성하기
    @Override
    public BigDecimal getDistance(Hub departureHub, Hub arrivalHub) {
        return new BigDecimal("50.5");
    }

    // TODO : 허브 간 이동 외부 API 연결 -> Impl 코드 완성하기
    @Override
    public Integer getDuration(BigDecimal distance) {
        return 40;
    }
}
