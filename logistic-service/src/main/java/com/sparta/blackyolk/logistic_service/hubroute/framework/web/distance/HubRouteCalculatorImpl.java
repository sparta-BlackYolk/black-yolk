package com.sparta.blackyolk.logistic_service.hubroute.framework.web.distance;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hubroute.application.port.HubRouteCalculator;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class HubRouteCalculatorImpl implements HubRouteCalculator {

    // TODO : Impl 코드 완성하기 - 미리 계산해놓은 것 반영시키는 코드로 작성하기
    @Override
    public BigDecimal getDistance(Hub departureHub, Hub arrivalHub) {
        return new BigDecimal("50.5");
    }

    // TODO : Impl 코드 완성하기 - 미리 계산해놓은 것 반영시키는 코드로 작성하기
    @Override
    public Integer getDuration(BigDecimal distance) {
        return 40;
    }
}
