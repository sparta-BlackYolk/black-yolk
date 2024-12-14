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


}
