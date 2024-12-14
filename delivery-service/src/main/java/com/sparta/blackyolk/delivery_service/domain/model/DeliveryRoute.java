package com.sparta.blackyolk.delivery_service.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_delivery_route")
public class DeliveryRoute extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "delivery_route_id", updatable = false, nullable = false)
    private UUID deliveryRouteId;

    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;
    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "delivery_manager_id", nullable = false)
    private UUID deliveryManagerId;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    private CurrentStatus currentStatus = CurrentStatus.HUB_WAITING; // 현재 상태 (HUB_WAITING, IN_DELIVERY, DELIVERED)

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;


    public static DeliveryRoute create(UUID deliveryId, UUID hubId, UUID deliveryManagerId, String remarks) {
        DeliveryRoute route = new DeliveryRoute();
        route.deliveryId = deliveryId;
        route.hubId = hubId;
        route.deliveryManagerId = deliveryManagerId;
        route.remarks = remarks;
        route.currentStatus = CurrentStatus.HUB_WAITING;
        route.isDeleted = false;
        return route;
    }


    public void updateStatus(CurrentStatus status) {
        this.currentStatus = status;
    }

    public void delete() {
        this.isDeleted = true;
    }
}