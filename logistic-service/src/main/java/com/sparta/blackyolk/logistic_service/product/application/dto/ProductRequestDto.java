package com.sparta.blackyolk.logistic_service.product.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    private String name;

    private Long company_id;

    private Long hub_id;

    private BigDecimal price;

    private int stockQuantity;

}
