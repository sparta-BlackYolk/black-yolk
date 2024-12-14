package com.sparta.msa_exam.order.order_service.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa_exam.order.order_service.application.dto.request.OrderSearchRequestDto;
import com.sparta.msa_exam.order.order_service.domain.model.Order;
import com.sparta.msa_exam.order.order_service.domain.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sparta.msa_exam.order.order_service.domain.model.QOrder.order;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, EntityManager entityManager) {
        this.orderJpaRepository = orderJpaRepository;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findByIdAndIsDeleteFalse(UUID orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public Page<Order> searchOrders(OrderSearchRequestDto requestDto) {
        int skip = (requestDto.page() - 1) * requestDto.limit();

        List<Order> results = queryFactory.selectFrom(order)
                .where(filterOrders(requestDto)
                        .and(order.isDelete.isFalse()))
                .orderBy(getOrderSpecifier(requestDto))
                .offset(skip)
                .limit(requestDto.limit())
                .fetch();

        long totalCount = getOrdersTotalCount(requestDto);

        return new PageImpl<>(results, requestDto.getPageable(), totalCount);
    }

    @Override
    public Page<Order> searchOrdersByCompanyIds(OrderSearchRequestDto requestDto, List<UUID> companyIds) {
        int skip = (requestDto.page() - 1) * requestDto.limit();

        List<Order> results = queryFactory.selectFrom(order)
                .where(filterOrdersByCompanyIds(requestDto, companyIds)
                        .and(order.isDelete.isFalse()))
                .orderBy(getOrderSpecifier(requestDto))
                .offset(skip)
                .limit(requestDto.limit())
                .fetch();

        long totalCount = getOrdersByCompanyIdsTotalCount(requestDto, companyIds);

        return new PageImpl<>(results, requestDto.getPageable(), totalCount);
    }

    private BooleanBuilder filterOrders(OrderSearchRequestDto requestDto) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (StringUtils.hasText(requestDto.searchValue())) {
            booleanBuilder.and(order.id.eq(UUID.fromString(requestDto.searchValue())));
        }
        return booleanBuilder;
    }

    private OrderSpecifier<?> getOrderSpecifier(OrderSearchRequestDto requestDto) {
        PathBuilder<Order> orderByExpression = new PathBuilder<>(Order.class, requestDto.orderBy());
        com.querydsl.core.types.Order direction = requestDto.sort().equals(Sort.Direction.DESC) ? com.querydsl.core.types.Order.DESC : com.querydsl.core.types.Order.ASC;
        return new OrderSpecifier(direction, orderByExpression);
    }

    private long getOrdersByCompanyIdsTotalCount(OrderSearchRequestDto requestDto, List<UUID> companyIds) {
        return queryFactory.select(Wildcard.count)
                .from(order)
                .where(filterOrdersByCompanyIds(requestDto, companyIds)
                        .and(order.isDelete.isFalse()))
                .fetch().get(0);
    }

    private BooleanBuilder filterOrdersByCompanyIds(OrderSearchRequestDto requestDto, List<UUID> companyIds) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if ("RECIPIENT_NAME".equals(requestDto.searchType())) {
            booleanBuilder.and(order.requestCompanyId.in(companyIds));
        }
        return booleanBuilder;
    }

    private long getOrdersTotalCount(OrderSearchRequestDto requestDto) {
        return queryFactory.select(Wildcard.count)
                .from(order)
                .where(filterOrders(requestDto)
                        .and(order.isDelete.isFalse()))
                .fetch().get(0);
    }

}