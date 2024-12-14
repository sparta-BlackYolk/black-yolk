package com.sparta.blackyolk.delivery_service.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_delivery")
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "delivery_id", updatable = false, nullable = false)
    private UUID deliveryId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "request_company_id", nullable = false)
    private UUID requestCompanyId;

    @Column(name = "supply_company_id", nullable = false)
    private UUID supplyCompanyId;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "receiver_name", nullable = false, length = 100)
    private String receiverName;

    @Column(name = "receiver_address", nullable = false, length = 255)
    private String receiverAddress;

    @Column(name = "receiver_slack_id", nullable = false, length = 100)
    private String receiverSlackId;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    private CurrentStatus currentStatus = CurrentStatus.HUB_WAITING;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public static Delivery create(UUID orderId, UUID requestCompanyId, UUID supplyCompanyId, UUID hubId,
                                  String receiverName, String receiverAddress, String receiverSlackId, String remarks) {
        Delivery delivery = new Delivery();
        delivery.orderId = orderId;
        delivery.requestCompanyId = requestCompanyId;
        delivery.supplyCompanyId = supplyCompanyId;
        delivery.hubId = hubId;
        delivery.receiverName = receiverName;
        delivery.receiverAddress = receiverAddress;
        delivery.receiverSlackId = receiverSlackId;
        delivery.remarks = remarks;
        delivery.currentStatus = CurrentStatus.HUB_WAITING;
        delivery.isDeleted = false;
        return delivery;
    }

    public void updateStatus(CurrentStatus status) {
        this.currentStatus = status;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
