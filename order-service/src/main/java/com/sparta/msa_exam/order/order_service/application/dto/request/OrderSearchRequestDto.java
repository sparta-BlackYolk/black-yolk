package com.sparta.msa_exam.order.order_service.application.dto.request;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa_exam.order.order_service.domain.model.Order;
import com.sparta.msa_exam.order.order_service.domain.model.OrderStatus;
import com.sparta.msa_exam.order.order_service.domain.repository.OrderJpaRepository;
import com.sparta.msa_exam.order.order_service.domain.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, EntityManager entityManager) {
        this.orderJpaRepository = orderJpaRepository;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Order> searchOrders(OrderSearchRequestDto requestDto) {
        int page = Math.max(requestDto.page(), 1);
        int skip = (page - 1) * requestDto.limit();

        List<Order> results = queryFactory.selectFrom(order)
                .where(filterOrders(requestDto)
                        .and(order.isDeleted.isFalse()))
                .orderBy(getOrderSpecifier(requestDto))
                .offset(skip)
                .limit(requestDto.limit())
                .fetch();

        long totalCount = Optional.ofNullable(
                queryFactory.select(Wildcard.count)
                        .from(order)
                        .where(filterOrders(requestDto)
                                .and(order.isDeleted.isFalse()))
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(results, requestDto.getPageable(), totalCount);
    }

    private BooleanBuilder filterOrders(OrderSearchRequestDto requestDto) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (StringUtils.hasText(requestDto.getSearchValue())) {
            booleanBuilder.and(order.id.eq(UUID.fromString(requestDto.getSearchValue())));
        }
        return booleanBuilder;
    }

    private OrderSpecifier<?> getOrderSpecifier(OrderSearchRequestDto requestDto) {
        String sortProperty = requestDto.getOrderBy().isEmpty() ? "createdAt" : requestDto.getOrderBy().get(0);
        PathBuilder<Order> orderByExpression = new PathBuilder<>(Order.class, "order");
        return new OrderSpecifier<>(
                requestDto.getSort() == Sort.Direction.DESC
                        ? com.querydsl.core.types.Order.DESC
                        : com.querydsl.core.types.Order.ASC,
                orderByExpression.get(sortProperty) // 🔥 get(String property) 사용
        );
    }
}