package com.sparta.msa_exam.order.order_service.infrastructure.repository;

import com.sparta.msa_exam.order.order_service.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

}