package com.sparta.msa_exam.order.order_service.domain.repository;

import com.sparta.msa_exam.order.order_service.application.dto.request.OrderSearchRequestDto;
import com.sparta.msa_exam.order.order_service.domain.model.Order;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository{
    Order save(Order order);

    Optional<Order> findByIdAndIsDeleteFalse(UUID orderId);

    Page<Order> searchOrders(OrderSearchRequestDto requestDto);

    Page<Order> searchOrdersByCompanyIds(OrderSearchRequestDto requestDto, List<UUID> companyIds);

}