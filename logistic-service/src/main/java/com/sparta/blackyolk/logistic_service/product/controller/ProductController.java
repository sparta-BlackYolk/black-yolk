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

    @GetMapping("/products/{product_id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable(name = "product_id") UUID product_id) {

        return ResponseEntity.ok(productService.getProduct(product_id));
    }

    @PutMapping("/products/{product_id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable(name = "product_id") UUID product_id,
                                                            @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto responseDto = productService.updateProduct(product_id, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/products/{product_id}/decrease-stock")
    public ResponseEntity<ProductResponseDto> decreaseStock(
            @PathVariable(name = "product_id") UUID product_id,
            @RequestParam int quantity) {
        ProductResponseDto responseDto = productService.decreaseStock(product_id, quantity);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/products/{product_id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "product_id") UUID product_id) {
        productService.deleteProduct(product_id);

        return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
    }

}
