package com.sparta.blackyolk.logistic_service.hub.application.domain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HubCoordinate implements Serializable {
    private BigDecimal axisX;
    private BigDecimal axisY;
}
