package com.sparta.blackyolk.logistic_service.hub.framework.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import com.sparta.blackyolk.logistic_service.hub.data.QHubEntity;
import com.sparta.blackyolk.logistic_service.hubroute.data.QHubRouteEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HubReadOnlyReadOnlyRepositoryImpl implements HubReadOnlyRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QHubEntity hubEntity = QHubEntity.hubEntity;

    /*
    select h from HubEntity h where h.hubId = :hubId and h.isDeleted = false
    */
    @Override
    public Optional<HubEntity> findByHubIdAndIsDeletedFalse(String hubId) {

        BooleanExpression isHubIdEquals = hubEntity.hubId.eq(hubId);
        BooleanExpression isNotDeleted = hubEntity.isDeleted.isFalse();

        HubEntity result = jpaQueryFactory.selectFrom(hubEntity)
            .where(isHubIdEquals.and(isNotDeleted))
            .fetchOne();

        return Optional.ofNullable(result);
    }

    /*
    select distinct h from HubEntity h where h.hubId in :hubIds and h.isDeleted = false
    */
    @Override
    public List<HubEntity> findByHubIdsAndIsDeletedFalse(List<String> hubIds) {
        return jpaQueryFactory
            .selectFrom(hubEntity)
            .where(buildConditions(hubIds))
            .fetch();
    }

    private BooleanBuilder buildConditions(List<String> hubIds) {

        BooleanBuilder builder = new BooleanBuilder();

        if (hubIds != null && !hubIds.isEmpty()) {
            builder.and(hubEntity.hubId.in(hubIds));
        }

        builder.and(hubEntity.isDeleted.isFalse());

        return builder;
    }

    /*
    select h from HubEntity h where h.hubCenter = :hubCenter and h.isDeleted = false
    */
    @Override
    public Optional<HubEntity> findByHubCenterIsDeletedFalse(String hubCenter) {

        BooleanExpression isHubCenterEquals = hubEntity.hubCenter.eq(hubCenter);
        BooleanExpression isNotDeleted = hubEntity.isDeleted.isFalse();

        HubEntity result = jpaQueryFactory.selectFrom(hubEntity)
            .where(isHubCenterEquals.and(isNotDeleted))
            .fetchOne();

        return Optional.ofNullable(result);
    }

    /*
    select distinct h.*
    from hub h
    join hub_route hr
      on hr.isDeleted = false
         and (hr.departureHubId = :hubId or hr.arrivalHubId = :hubId)
    where h.hubId = :hubId
      and h.isDeleted = false;
    */
    @Override
    public Optional<HubEntity> findByHubIdAndIsDeletedFalseWithHubRoutes(String hubId) {

        QHubRouteEntity hubRouteEntity = QHubRouteEntity.hubRouteEntity;

        BooleanExpression isHubRouteNotDeleted = hubRouteEntity.isDeleted.isFalse();
        BooleanExpression isDepartureHub = hubRouteEntity.departureHub.hubId.eq(hubId);
        BooleanExpression isArrivalHub = hubRouteEntity.arrivalHub.hubId.eq(hubId);
        BooleanExpression isHubIdEquals = hubEntity.hubId.eq(hubId);
        BooleanExpression isHubNotDeleted = hubEntity.isDeleted.isFalse();

        BooleanExpression isDepartureOrArrivalHub = isDepartureHub.or(isArrivalHub);

        HubEntity result = jpaQueryFactory.selectFrom(hubEntity).distinct()
            .join(hubRouteEntity).on(isHubRouteNotDeleted.and(isDepartureOrArrivalHub)).fetchJoin()
            .where(isHubIdEquals.and(isHubNotDeleted))
            .fetchOne();

        return Optional.ofNullable(result);
    }

    /*
    select h
    from HubEntity h
    where h.isDeleted = false
      and (
          lower(h.hubName) like concat('%', :keyword, '%') or
          lower(h.hubAddress.sido) like concat('%', :keyword, '%') or
          lower(h.hubAddress.sigungu) like concat('%', :keyword, '%') or
          lower(h.hubAddress.eupmyun) like concat('%', :keyword, '%') or
          lower(h.hubAddress.roadName) like concat('%', :keyword, '%') or
          lower(h.hubAddress.buildingNumber) like concat('%', :keyword, '%') or
          lower(h.hubAddress.zipCode) like concat('%', :keyword, '%')
      )
    order by h.createdAt desc

    select count(h)
    from HubEntity h
    where h.isDeleted = false
      and (h.hubName like concat('%', :keyword, '%') or :keyword is null)
    */
    @Override
    public Page<HubEntity> findAllHubsAndIsDeletedFalseWithKeyword(
        String keyword,
        Pageable pageable
    ) {

        BooleanBuilder builder = buildKeywordSearchConditions(keyword);

        List<OrderSpecifier<?>> orderSpecifiers = pageable.getSort().stream()
            .map(order -> mapToOrderSpecifier(order.getProperty(), order.isAscending()))
            .collect(Collectors.toList());

        List<HubEntity> results = jpaQueryFactory.selectFrom(hubEntity)
            .where(builder)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCount = jpaQueryFactory.select(hubEntity.count())
            .from(hubEntity)
            .where(builder)
            .fetchOne();

        long total = (totalCount != null) ? totalCount : 0L;

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanBuilder buildKeywordSearchConditions(String keyword) {

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(hubEntity.isDeleted.isFalse());

        if (keyword != null && !keyword.isBlank()) {
            String trimmedKeyword = keyword.trim();

            // 여러 필드에 대한 검색 조건 추가
            builder.and(
                hubEntity.hubName.containsIgnoreCase(trimmedKeyword)
                    .or(hubEntity.hubAddress.sido.containsIgnoreCase(trimmedKeyword))
                    .or(hubEntity.hubAddress.sigungu.containsIgnoreCase(trimmedKeyword))
                    .or(hubEntity.hubAddress.eupmyun.containsIgnoreCase(trimmedKeyword))
                    .or(hubEntity.hubAddress.roadName.containsIgnoreCase(trimmedKeyword))
                    .or(hubEntity.hubAddress.buildingNumber.containsIgnoreCase(trimmedKeyword))
                    .or(hubEntity.hubAddress.zipCode.containsIgnoreCase(trimmedKeyword))
            );
        }

        return builder;
    }

    private OrderSpecifier<?> mapToOrderSpecifier(String sortProperty, boolean ascending) {

        log.info("[Hub QueryDSL] Applying sort: {} ascending: {}", sortProperty, ascending);

        return switch (sortProperty) {
            case "createdAt" -> ascending ? hubEntity.createdAt.asc() : hubEntity.createdAt.desc();
            case "updatedAt" -> ascending ? hubEntity.updatedAt.asc() : hubEntity.updatedAt.desc();
            default -> hubEntity.createdAt.desc();
        };
    }
}
