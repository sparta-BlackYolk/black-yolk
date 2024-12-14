package com.sparta.blackyolk.logistic_service.product.repository;

import com.sparta.blackyolk.logistic_service.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
