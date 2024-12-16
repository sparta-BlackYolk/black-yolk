package com.sparta.blackyolk.logistic_service.product.entity;

import com.sparta.blackyolk.logistic_service.product.application.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long company_id;

    @Column(nullable = false)
    private Long hub_id;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private Boolean is_deleted;

    @PreUpdate
    public void updateDeleteField() {
        if (this.is_deleted && super.getDeletedAt() == null) {
            super.markDeleted();
        }
    }

    public static Product create(ProductRequestDto requestDto) {
        return Product.builder()
                .name(requestDto.getName())
                .company_id(requestDto.getCompany_id())
                .hub_id(requestDto.getHub_id())
                .price(requestDto.getPrice())
                .stockQuantity(requestDto.getStockQuantity())
                .is_deleted(false)
                .build();
    }

    public void update(ProductRequestDto requestDto) {
        this.name = requestDto.getName();
        this.company_id = requestDto.getCompany_id();
        this.hub_id = requestDto.getHub_id();
        this.price = requestDto.getPrice();
        this.stockQuantity = requestDto.getStockQuantity();
    }

    public void decreaseStock(int quantity) {
        this.stockQuantity = this.getStockQuantity() - quantity;
    }

    public void delete() {
        this.is_deleted = true;
    }






}
