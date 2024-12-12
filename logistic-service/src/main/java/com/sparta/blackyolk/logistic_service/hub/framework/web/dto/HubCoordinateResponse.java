package com.sparta.blackyolk.logistic_service.hub.framework.web.dto;

import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubCoordinate;
import java.math.BigDecimal;

public record HubCoordinateResponse(
    BigDecimal axisX,
    BigDecimal axisY
) {
    public static HubCoordinateResponse fromDomain(HubCoordinate hubCoordinate) {
        return new HubCoordinateResponse(
            hubCoordinate.getAxisX(),
            hubCoordinate.getAxisY()
        );
    }
}
