package com.sparta.msa_exam.order.order_service.domain.repository;

import com.sparta.msa_exam.order.order_service.domain.model.Order;
import com.sparta.msa_exam.order.order_service.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

    // 논리 삭제가 되지 않은 주문만 조회
    Optional<Order> findByIdAndIsDeletedFalse(UUID orderId);
}