package com.sparta.msa_exam.order.order_service.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa_exam.order.order_service.application.dto.request.OrderSearchRequestDto;
import com.sparta.msa_exam.order.order_service.domain.model.Order;
import com.sparta.msa_exam.order.order_service.domain.model.QOrder;
import com.sparta.msa_exam.order.order_service.domain.model.Status;
import com.sparta.msa_exam.order.order_service.domain.repository.OrderJpaRepository;
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

    /**
     * 🔥 주문 저장 메서드
     */
    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    /**
     * 🔥 ID로 삭제되지 않은 주문 조회
     */
    @Override
    public Optional<Order> findByIdAndIsDeletedFalse(UUID orderId) {
        return orderJpaRepository.findById(orderId);
    }

    /**
     * 🔥 다중 조건 검색 및 페이징
     */
    @Override
    public Page<Order> searchOrders(OrderSearchRequestDto requestDto) {
        int skip = (requestDto.page() - 1) * requestDto.limit();

        List<Order> results = queryFactory.selectFrom(order)
                .where(filterOrders(requestDto)
                        .and(order.isDeleted.isFalse()))
                .orderBy(getOrderSpecifier(requestDto))
                .offset(skip)
                .limit(requestDto.limit())
                .fetch();

        long totalCount = queryFactory.select(Wildcard.count)
                .from(order)
                .where(filterOrders(requestDto).and(order.isDeleted.isFalse()))
                .fetchOne();

        return new PageImpl<>(results, requestDto.getPageable(), totalCount != null ? totalCount : 0L);
    }

    /**
     * 🔥 회사 ID 목록으로 주문 조회
     */
    @Override
    public Page<Order> searchOrdersByCompanyIds(OrderSearchRequestDto requestDto, List<UUID> companyIds) {
        int skip = (requestDto.page() - 1) * requestDto.limit();

        List<Order> results = queryFactory.selectFrom(order)
                .where(filterOrdersByCompanyIds(requestDto, companyIds)
                        .and(order.isDeleted.isFalse()))
                .orderBy(getOrderSpecifier(requestDto))
                .offset(skip)
                .limit(requestDto.limit())
                .fetch();

        long totalCount = getOrdersByCompanyIdsTotalCount(requestDto, companyIds);

        return new PageImpl<>(results, requestDto.getPageable(), totalCount);
    }

    /**
     * 🔥 검색 조건 필터링 (주문 ID, 주문 상태 등)
     */
    private BooleanBuilder filterOrders(OrderSearchRequestDto requestDto) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        // 🔥 주문 ID로 필터링
        if (StringUtils.hasText(requestDto.getSearchValue())) {
            booleanBuilder.and(order.id.eq(UUID.fromString(requestDto.getSearchValue())));
        }

        // 🔥 주문 상태로 필터링
        if (requestDto.getOrderStatus() != null) {
            booleanBuilder.and(order.orderStatus.eq(requestDto.getOrderStatus()));
        }

        return booleanBuilder;
    }

    /**
     * 🔥 동적 정렬 기능
     */
    private OrderSpecifier<?> getOrderSpecifier(OrderSearchRequestDto requestDto) {
        // 🔥 정렬 기준 기본값 (createdAt)
        String sortProperty = requestDto.getOrderBy().isEmpty() ? "createdAt" : requestDto.getOrderBy().get(0);

        // 🔥 PathBuilder<Order>로 생성
        PathBuilder<Order> orderByExpression = new PathBuilder<>(Order.class, "order");

        // 🔥 동적 정렬 조건 반환
        return new OrderSpecifier<>(
                requestDto.getSort() == Sort.Direction.DESC
                        ? com.querydsl.core.types.Order.DESC
                        : com.querydsl.core.types.Order.ASC,
                orderByExpression.get(sortProperty)
        );
    }

    /**
     * 🔥 특정 회사에 대한 주문 총 개수 계산
     */
    private long getOrdersByCompanyIdsTotalCount(OrderSearchRequestDto requestDto, List<UUID> companyIds) {
        return queryFactory.select(Wildcard.count)
                .from(order)
                .where(filterOrdersByCompanyIds(requestDto, companyIds)
                        .and(order.isDeleted.isFalse()))
                .fetchOne() != null ? queryFactory.select(Wildcard.count)
                .from(order)
                .where(filterOrdersByCompanyIds(requestDto, companyIds)
                        .and(order.isDeleted.isFalse()))
                .fetchOne() : 0L;
    }

    /**
     * 🔥 회사 ID 목록으로 주문 필터링
     */
    private BooleanBuilder filterOrdersByCompanyIds(OrderSearchRequestDto requestDto, List<UUID> companyIds) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if ("REQUESTER_NAME".equals(requestDto.getSearchType())) {
            booleanBuilder.and(order.requestCompanyId.in(companyIds));
        }

        return booleanBuilder;
    }

    /**
     * 🔥 전체 주문 총 개수 계산
     */
    private long getOrdersTotalCount(OrderSearchRequestDto requestDto) {
        return queryFactory.select(Wildcard.count)
                .from(order)
                .where(filterOrders(requestDto)
                        .and(order.isDeleted.isFalse()))
                .fetchOne() != null ? queryFactory.select(Wildcard.count)
                .from(order)
                .where(filterOrders(requestDto)
                        .and(order.isDeleted.isFalse()))
                .fetchOne() : 0L;
    }
}