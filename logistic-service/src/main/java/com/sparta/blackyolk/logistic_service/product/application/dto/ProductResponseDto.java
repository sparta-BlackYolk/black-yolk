package com.sparta.blackyolk.logistic_service.product.application.dto;

import com.sparta.blackyolk.logistic_service.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class ProductResponseDto {

    private UUID id;

    private String name;

    private Long company_id;

    private Long hub_id;

    private BigDecimal price;

    private int stockQuantity;

    public static ProductResponseDto toDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .company_id(product.getCompany_id())
                .hub_id(product.getHub_id())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();
    }
}
