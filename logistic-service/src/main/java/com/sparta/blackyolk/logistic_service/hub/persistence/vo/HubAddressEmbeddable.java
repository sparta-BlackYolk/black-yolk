package com.sparta.blackyolk.logistic_service.hub.persistence.vo;

import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubAddress;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Optional;
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

    public void updateAddress(
        String sido,
        String sigungu,
        String eupmyun,
        String roadName,
        String buildingNumber,
        String zipCode
    ) {
        Optional.ofNullable(sido).ifPresent(value -> this.sido = value);
        Optional.ofNullable(sigungu).ifPresent(value -> this.sigungu = value);
        Optional.ofNullable(eupmyun).ifPresent(value -> this.eupmyun = value);
        Optional.ofNullable(roadName).ifPresent(value -> this.roadName = value);
        Optional.ofNullable(buildingNumber).ifPresent(value -> this.buildingNumber = value);
        Optional.ofNullable(zipCode).ifPresent(value -> this.zipCode = value);
    }
}
