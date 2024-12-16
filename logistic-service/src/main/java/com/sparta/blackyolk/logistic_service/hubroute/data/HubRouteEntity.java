package com.sparta.blackyolk.logistic_service.hubroute.data;

import com.sparta.blackyolk.logistic_service.common.BaseEntity;
import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.HubRoute;
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
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
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
    @Column(name = "hub_route_status", nullable = false, columnDefinition = "varchar(255)")
    private HubRouteStatus status = HubRouteStatus.ACTIVE;

    @Column(name = "duration", nullable = false)
    private Long duration; // milliseconds

    @Column(name = "distance", precision = 10, scale = 2, nullable = false)
    private Long distance; // meters

    @PrePersist
    private void prePersistence() {
        if (hubRouteId == null || hubRouteId.isEmpty()) {
            hubRouteId = UUID.randomUUID().toString();
        }
    }

    public HubRouteEntity(
        String userId,
        HubEntity departureHub,
        HubEntity arrivalHub,
        Long duration,
        Long distance
    ) {
        super(userId, userId);
        this.hubRouteId = null;
        this.departureHub = departureHub;
        this.arrivalHub = arrivalHub;
        this.duration = duration;
        this.distance = distance;
    }

    public static HubRouteEntity toEntity(
        String userId,
        HubEntity departureHubEntity,
        HubEntity arrivalHubEntity,
        HubRoute hubRoute
    ) {
        return new HubRouteEntity(
            userId,
            departureHubEntity,
            arrivalHubEntity,
            hubRoute.getDuration(),
            hubRoute.getDistance()
        );
    }

    // TODO: 각각의 departure, arrival HubEntity를 toDomain으로 변경하는 것이 과하지 않은가? 확인
    public HubRoute toDomain() {
        return new HubRoute(
            this.hubRouteId,
            this.departureHub.toDomain(),
            this.arrivalHub.toDomain(),
            this.status,
            this.duration,
            this.distance
        );
    }

    public void update(HubRouteForUpdate hubRouteForUpdate) {
        super.updateFrom(hubRouteForUpdate.userId());
        Optional.ofNullable(hubRouteForUpdate.status()).ifPresent(value -> this.status = value);
    }

    public void delete(String userId) {
        this.status = HubRouteStatus.INACTIVE;
        super.deleteFrom(userId);
    }
}
