package com.sparta.msa_exam.order.order_service.domain.client;

import com.sparta.msa_exam.order.order_service.application.dto.response.ProductInfoResponseDto;
import com.sparta.msa_exam.order.order_service.application.dto.response.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name="product-service")
public interface ProductClient {

    @PostMapping("/products/batch")
    List<ProductResponseDto> findProductsByIds(@RequestBody List<UUID> ids);

    @GetMapping("/products/{productId}")
    ProductInfoResponseDto findProductById(@PathVariable("productId") UUID productId);

    @GetMapping("/products/{productId}/amount")
    ProductResponseDto checkAmount(@PathVariable UUID productId, Integer quantity);
}