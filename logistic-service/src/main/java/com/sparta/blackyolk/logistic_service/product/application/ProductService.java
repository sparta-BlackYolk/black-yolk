package com.sparta.blackyolk.logistic_service.product.application;

import com.sparta.blackyolk.logistic_service.product.application.dto.ProductRequestDto;
import com.sparta.blackyolk.logistic_service.product.application.dto.ProductResponseDto;
import com.sparta.blackyolk.logistic_service.product.entity.Product;
import com.sparta.blackyolk.logistic_service.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = productRepository.save(Product.create(requestDto));

        return ProductResponseDto.toDto(product);
    }

    public ProductResponseDto getProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 정보가 없습니다."));

        return ProductResponseDto.toDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(UUID productId, ProductRequestDto requestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 정보가 없습니다."));

        product.update(requestDto);
        return ProductResponseDto.toDto(product);
    }

    @Transactional
    public ProductResponseDto decreaseStock(UUID productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 정보가 없습니다."));

        if (product.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("재고 수량이 부족합니다.");
        }

        product.decreaseStock(quantity);

        return ProductResponseDto.toDto(product);

    }

    @Transactional
    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 정보가 없습니다."));

        product.delete();
    }

}
