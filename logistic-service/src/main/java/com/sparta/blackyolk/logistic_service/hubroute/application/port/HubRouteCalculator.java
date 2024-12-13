package com.sparta.blackyolk.logistic_service.hubroute.application.port;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import java.math.BigDecimal;

public interface HubRouteCalculator {
    BigDecimal getDistance(Hub departureHub, Hub arrivalHub);
    Integer getDuration(BigDecimal distance);
}
