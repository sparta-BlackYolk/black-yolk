package com.sparta.msa_exam.order.order_service.domain.repository;

import com.sparta.msa_exam.order.order_service.application.dto.request.OrderSearchRequestDto;
import com.sparta.msa_exam.order.order_service.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderRepository {
    // 주문 조건 검색 (페이징 포함)
    Page<Order> searchOrders(OrderSearchRequestDto requestDto);

    // 특정 회사에 대한 주문 검색
    Page<Order> searchOrdersByCompanyIds(OrderSearchRequestDto requestDto, List<UUID> companyIds);
}