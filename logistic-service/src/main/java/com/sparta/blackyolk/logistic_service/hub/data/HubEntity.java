package com.sparta.blackyolk.logistic_service.hub.data;

import com.sparta.blackyolk.logistic_service.common.BaseEntity;
import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForUpdate;
import com.sparta.blackyolk.logistic_service.hub.data.vo.HubAddressEmbeddable;
import com.sparta.blackyolk.logistic_service.hub.data.vo.HubCoordinateEmbeddable;
import com.sparta.blackyolk.logistic_service.hub.data.vo.HubStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "p_hub")
public class HubEntity extends BaseEntity {

    @Id
    @Column(name = "hub_id", nullable = false, updatable = false)
    private String hubId;

    @Column(name = "hub_name", unique = true, nullable = false)
    private String hubName;

    @Column(name = "hub_center", nullable = false)
    private String hubCenter;

    @Enumerated(EnumType.STRING)
    @Column(name = "hub_status", nullable = false)
    private HubStatus status = HubStatus.ACTIVE;

    @Embedded
    private HubCoordinateEmbeddable hubCoordinate;

    @Embedded
    private HubAddressEmbeddable hubAddress;

    @Column(name = "user_id", nullable = false)
    private String hubManagerId;

    @PrePersist
    private void prePersistence() {
        if (hubId == null || hubId.isEmpty()) {
            hubId = UUID.randomUUID().toString();
        }
    }

    @Builder
    private HubEntity(
        String userId,
        String hubManagerId,
        String hubName,
        String hubCenter,
        HubCoordinateEmbeddable hubCoordinate,
        HubAddressEmbeddable hubAddress
    ) {
        super(userId, userId);
        this.hubManagerId = hubManagerId;
        this.hubName = hubName;
        this.hubCenter = hubCenter;
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
            .hubCenter(hubForCreate.center())
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
            this.hubCenter,
            this.status,
            this.hubCoordinate.toDomain(),
            this.hubAddress.toDomain(),
            this.hubManagerId,
            super.isDeleted()
        );
    }

    public void updateHub(
        HubForUpdate hubForUpdate,
        BigDecimal axisX,
        BigDecimal axisY
    ) {
        super.updateFrom(hubForUpdate.userId());

        Optional.ofNullable(hubForUpdate.hubManagerId()).ifPresent(value -> this.hubManagerId = value);
        Optional.ofNullable(hubForUpdate.name()).ifPresent(value -> this.hubName = value);
        Optional.ofNullable(hubForUpdate.status()).ifPresent(value -> this.status = value);

        hubCoordinate.updateCoordinate(axisX, axisY);

        if (hubForUpdate.address() != null) {
            hubAddress.updateAddress(
                hubForUpdate.address().sido(),
                hubForUpdate.address().sigungu(),
                hubForUpdate.address().eupmyun(),
                hubForUpdate.address().roadName(),
                hubForUpdate.address().buildingNumber(),
                hubForUpdate.address().zipCode()
            );
        }
    }

    public void deleteHub(String userId) {
        this.status = HubStatus.INACTIVE;
        super.deleteFrom(userId);
    }
}
