package com.sparta.blackyolk.logistic_service.hubroute.data;

import com.sparta.blackyolk.logistic_service.common.BaseEntity;
import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForCreate;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForDelete;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRouteForUpdate;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "p_hub_route")
public class HubRouteEntity extends BaseEntity {

    @Id
    @Column(name = "hub_route_id", nullable = false, updatable = false)
    private String hubRouteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_hub_id", referencedColumnName = "hub_id", nullable = false)
    private HubEntity departureHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_hub_id", referencedColumnName = "hub_id", nullable = false)
    private HubEntity arrivalHub;

    @Enumerated(EnumType.STRING)
    @Column(name = "hub_status", nullable = false, columnDefinition = "varchar(255)")
    private HubRouteStatus status = HubRouteStatus.ACTIVE;

    @Column(name = "duration", nullable = false)
    private Integer duration; // 분

    @Column(name = "distance", precision = 10, scale = 2, nullable = false)
    private BigDecimal distance; // km

    @Column(name = "time_slot", nullable = false, columnDefinition = "varchar(255)")
    private String timeSlot; // ex. 09:00-12:00

    @Column(name = "time_slot_weight", nullable = false)
    private double timeSlotWeight; // 시간대 별 가중치

    @PrePersist
    private void prePersistence() {
        if (hubRouteId == null || hubRouteId.isEmpty()) {
            hubRouteId = UUID.randomUUID().toString();
        }
    }

    public HubRouteEntity(
        Long userId,
        HubEntity departureHub,
        HubEntity arrivalHub,
        Integer duration,
        BigDecimal distance,
        String timeSlot,
        double timeSlotWeight
    ) {
        super(userId, userId);
        this.hubRouteId = null;
        this.departureHub = departureHub;
        this.arrivalHub = arrivalHub;
        this.duration = duration;
        this.distance = distance;
        this.timeSlot = timeSlot;
        this.timeSlotWeight = timeSlotWeight;
    }

    public static HubRouteEntity toEntity(
        HubRouteForCreate hubRouteForCreate,
        HubEntity departureHubEntity,
        HubEntity arrivalHubEntity,
        BigDecimal distance,
        Integer duration
    ) {
        return new HubRouteEntity(
            hubRouteForCreate.userId(),
            departureHubEntity,
            arrivalHubEntity,
            duration,
            distance,
            hubRouteForCreate.timeSlot(),
            hubRouteForCreate.timeSlotWeight()
        );
    }

    // TODO: 각각의 departure, arrival HubEntity를 toDomain으로 변경하는 것이 과하지 않은가? 확인
    public HubRoute toDomain() {
        return new HubRoute(
            this.hubRouteId,
            this.departureHub.toDomain(),
            this.arrivalHub.toDomain(),
            this.status,
            this.timeSlot,
            this.duration,
            this.distance,
            this.timeSlotWeight
        );
    }

    public void update(HubRouteForUpdate hubRouteForUpdate, HubEntity arrivalHubEntity, BigDecimal distance, Integer duration) {
        super.updateFrom(hubRouteForUpdate.userId());
        Optional.ofNullable(arrivalHubEntity).ifPresent(value -> this.arrivalHub = value);
        Optional.ofNullable(hubRouteForUpdate.timeSlot()).ifPresent(value -> this.timeSlot = value);
        Optional.ofNullable(hubRouteForUpdate.timeSlotWeight()).ifPresent(value -> this.timeSlotWeight = value);
        Optional.ofNullable(distance).ifPresent(value -> this.distance = value);
        Optional.ofNullable(duration).ifPresent(value -> this.duration = value);
        Optional.ofNullable(hubRouteForUpdate.status()).ifPresent(value -> this.status = value);
    }

    public void delete(HubRouteForDelete hubRouteForDelete) {
        this.status = HubRouteStatus.INACTIVE; // TODO : domain으로 옮길 수 있지 않을까?
        super.deleteFrom(hubRouteForDelete.userId());
    }
}
