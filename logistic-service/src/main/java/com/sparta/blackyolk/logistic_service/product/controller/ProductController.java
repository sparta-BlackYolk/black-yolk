package com.sparta.blackyolk.logistic_service.product.controller;

import com.sparta.blackyolk.logistic_service.product.application.ProductService;
import com.sparta.blackyolk.logistic_service.product.application.dto.ProductRequestDto;
import com.sparta.blackyolk.logistic_service.product.application.dto.ProductResponseDto;
import com.sparta.blackyolk.logistic_service.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto requestDto) {
        ProductResponseDto responseDto = productService.createProduct(requestDto);

        return ResponseEntity.ok(responseDto);
    }



}
