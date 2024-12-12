package com.sparta.blackyolk.logistic_service.hub.persistence;

import com.sparta.blackyolk.logistic_service.common.BaseEntity;
import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.persistence.vo.HubAddressEmbeddable;
import com.sparta.blackyolk.logistic_service.hub.persistence.vo.HubCoordinateEmbeddable;
import com.sparta.blackyolk.logistic_service.hub.persistence.vo.HubStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "p_hub")
public class HubEntity extends BaseEntity {

    @Id
    @Column(name = "hub_id", nullable = false, updatable = false)
    private String hubId;

    @Column(name = "hub_name", unique = true, nullable = false)
    private String hubName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    private HubStatus status = HubStatus.ACTIVE;

    @Embedded
    private HubCoordinateEmbeddable hubCoordinate;

    @Embedded
    private HubAddressEmbeddable hubAddress;

    @Column(name = "user_id", nullable = false)
    private Long hubManagerId;

    @PrePersist
    private void prePersistence() {
        if (hubId == null || hubId.isEmpty()) {
            hubId = UUID.randomUUID().toString();
        }
    }

    @Builder
    private HubEntity(
        Long userId,
        Long hubManagerId,
        String hubName,
        HubCoordinateEmbeddable hubCoordinate,
        HubAddressEmbeddable hubAddress
    ) {
        super(userId, userId);
        this.hubManagerId = hubManagerId;
        this.hubName = hubName;
        this.hubCoordinate = hubCoordinate;
        this.hubAddress = hubAddress;
    }

    public static HubEntity toEntity(
        HubForCreate hubForCreate,
        BigDecimal axisX,
        BigDecimal axisY
    ) {
        return HubEntity.builder()
            .userId(hubForCreate.userId())
            .hubName(hubForCreate.name())
            .hubCoordinate(new HubCoordinateEmbeddable(axisX, axisY))
            .hubAddress(HubAddressEmbeddable.builder()
                .sido(hubForCreate.sido())
                .sigungu(hubForCreate.sigungu())
                .eupmyun(hubForCreate.eupmyun())
                .roadName(hubForCreate.roadName())
                .buildingNumber(hubForCreate.buildingNumber())
                .zipCode(hubForCreate.zipCode())
                .build()
            )
            .hubManagerId(hubForCreate.hubManagerId())
            .build();
    }

    public Hub toDomain() {
        return new Hub(
            this.hubId,
            this.hubName,
            this.status,
            this.hubCoordinate.toDomain(),
            this.hubAddress.toDomain(),
            this.hubManagerId,
            super.isDeleted()
        );
    }
}
