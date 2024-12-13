package com.sparta.msa_exam.order.order_service.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "request_company_id", nullable = false)
    private UUID requestCompanyId;

    @Column(name = "supply_company_id", nullable = false)
    private UUID supplyCompanyId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "remarks", length = 255)
    private String remarks;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    public static Order createOrder(UUID requestCompanyId, UUID supplyCompanyId, UUID productId,
                                    Integer quantity, String remarks) {
        return Order.builder()
                .requestCompanyId(requestCompanyId)
                .supplyCompanyId(supplyCompanyId)
                .productId(productId)
                .quantity(quantity)
                .remarks(remarks)
                .build();
    }

    public void cancelOrder() {
        this.isDeleted = true;
    }
}