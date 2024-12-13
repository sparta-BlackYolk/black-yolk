package com.sparta.blackyolk.logistic_service.hub.application.domain.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HubCoordinate {
    private final BigDecimal axisX;
    private final BigDecimal axisY;
}
