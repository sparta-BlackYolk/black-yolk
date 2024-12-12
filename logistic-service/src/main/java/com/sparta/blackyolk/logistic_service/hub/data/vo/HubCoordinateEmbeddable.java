package com.sparta.blackyolk.logistic_service.hub.data.vo;

import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubCoordinate;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class HubCoordinateEmbeddable {

    @Column(name = "axis_x", precision = 11, scale = 8, nullable = false)
    private BigDecimal axisX;

    @Column(name = "axis_y", precision = 10, scale = 8, nullable = false)
    private BigDecimal axisY;

    public HubCoordinate toDomain() {
        return new HubCoordinate(
            this.axisX,
            this.axisY
        );
    }

    public void updateCoordinate(
        BigDecimal axisX,
        BigDecimal axisY
    ) {
        this.axisX = axisX;
        this.axisY = axisY;
    }
}
