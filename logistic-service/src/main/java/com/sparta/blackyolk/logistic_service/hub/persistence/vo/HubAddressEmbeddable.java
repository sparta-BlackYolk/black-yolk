package com.sparta.blackyolk.logistic_service.hub.persistence.vo;

import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubAddress;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class HubAddressEmbeddable {

    @Column(name = "sido", nullable = false)
    private String sido;

    @Column(name = "sigungu", nullable = false)
    private String sigungu;

    @Column(name = "eupmyun")
    private String eupmyun;

    @Column(name = "road_name", nullable = false)
    private String roadName;

    @Column(name = "building_number", nullable = false)
    private String buildingNumber;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    public HubAddress toDomain() {
        return new HubAddress(
            this.sido,
            this.sigungu,
            this.eupmyun,
            this.roadName,
            this.buildingNumber,
            this.zipCode
        );
    }
}
