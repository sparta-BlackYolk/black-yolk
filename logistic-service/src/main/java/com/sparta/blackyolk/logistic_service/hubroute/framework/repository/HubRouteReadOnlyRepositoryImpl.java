package com.sparta.blackyolk.logistic_service.hubroute.framework.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.blackyolk.logistic_service.common.exception.CustomException;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.hub.data.QHubEntity;
import com.sparta.blackyolk.logistic_service.hubroute.data.HubRouteEntity;
import com.sparta.blackyolk.logistic_service.hubroute.data.QHubRouteEntity;
import com.sparta.blackyolk.logistic_service.hubroute.data.vo.HubRouteStatus;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HubRouteReadOnlyRepositoryImpl implements HubRouteReadOnlyRepository{

    private final JPAQueryFactory jpaQueryFactory;
    private final QHubRouteEntity hubRouteEntity = QHubRouteEntity.hubRouteEntity;

    /*
    select hr from
    HubRouteEntity hr
    join fetch hr.departureHub
    join fetch hr.arrivalHub
    where hr.hubRouteId = :hubRouteId
      and hr.isDeleted = false
    * */
    @Override
    public Optional<HubRouteEntity> findByHubRouteIdAndIsDeletedFalse(String hubRouteId) {

        QHubEntity departureHub =  new QHubEntity("departureHub");
        QHubEntity arrivalHub = new QHubEntity("arrivalHub");

        BooleanExpression isHubRouteIdEquals = hubRouteEntity.hubRouteId.eq(hubRouteId);
        BooleanExpression isDeleted = hubRouteEntity.isDeleted.eq(false);

        HubRouteEntity result = jpaQueryFactory.selectFrom(hubRouteEntity)
            .join(hubRouteEntity.departureHub, departureHub).fetchJoin()
            .join(hubRouteEntity.arrivalHub, arrivalHub).fetchJoin()
            .where(isHubRouteIdEquals.and(isDeleted))
            .fetchOne();

        return Optional.ofNullable(result);
    }

    /*
    select
        h.*
    from
        hub_route_entity h
    join
        hub_entity d on h.departure_hub_id = d.hub_id
    join
        hub_entity a on h.arrival_hub_id = a.hub_id
    where
        d.hub_id = :departureHubId
        and a.hub_id = :arrivalHubId
        and h.is_deleted = false
        and h.status = 'ACTIVE'
    limit 1;
    */
    @Override
    public Optional<HubRouteEntity> findByDepartureHubIdAndArrivalHubId(String departureHubId, String arrivalHubId) {

        BooleanExpression isDepartureHub = hubRouteEntity.departureHub.hubId.eq(departureHubId);
        BooleanExpression isArrivalHub = hubRouteEntity.arrivalHub.hubId.eq(arrivalHubId);
        BooleanExpression isDeleted = hubRouteEntity.isDeleted.eq(false);
        BooleanExpression isActive = hubRouteEntity.status.eq(HubRouteStatus.ACTIVE);

        HubRouteEntity result = jpaQueryFactory.selectFrom(hubRouteEntity)
            .where(
                isDepartureHub,
                isArrivalHub,
                isDeleted,
                isActive
            ).fetchOne();

        return Optional.ofNullable(result);
    }

    /*
    select
        h.*
    from
        hub_route_entity h
    left join
        hub_entity departure on h.departure_hub_id = departure.hub_id
    left join
        hub_entity arrival on h.arrival_hub_id = arrival.hub_id
    where
        h.is_deleted = false
        and h.status = 'active';
    */
    @Override
    public List<HubRouteEntity> findAllHubRoutesAndIsDeletedFalseAndActive() {

        BooleanExpression isDeleted = hubRouteEntity.isDeleted.eq(false);
        BooleanExpression isActive = hubRouteEntity.status.eq(HubRouteStatus.ACTIVE);

        QHubEntity departureHubAlias = new QHubEntity("departureHubAlias");
        QHubEntity arrivalHubAlias = new QHubEntity("arrivalHubAlias");

        return jpaQueryFactory.selectFrom(hubRouteEntity)
            .join(hubRouteEntity.departureHub, departureHubAlias).fetchJoin()
            .join(hubRouteEntity.arrivalHub, arrivalHubAlias).fetchJoin()
            .where(isDeleted, isActive)
            .fetch();
    }

    /*
    select hr.*
    from hubRouteEntity hr
    join hubEntity departureHubAlias on hr.departureHubId = departureHubAlias.hubId
    join hubEntity arrivalHubAlias on hr.arrivalHubId = arrivalHubAlias.hubId
    where hr.isDeleted = false
      and hr.status = 'ACTIVE'
      and (hr.departureHubId = :hubId or hr.arrivalHubId = :hubId);
    */
    @Override
    public List<HubRouteEntity> findAllByHubIdAndIsDeletedFalseAndActive(String hubId) {

        log.info("[허브 경로 조회] hubId: {}", hubId);

        BooleanExpression isNotDeleted = hubRouteEntity.isDeleted.eq(false);
        BooleanExpression isActive = hubRouteEntity.status.eq(HubRouteStatus.ACTIVE);
        BooleanExpression matchesHubId = hubRouteEntity.departureHub.hubId.eq(hubId)
            .or(hubRouteEntity.arrivalHub.hubId.eq(hubId));

        QHubEntity departureHubAlias = new QHubEntity("departureHubAlias");
        QHubEntity arrivalHubAlias = new QHubEntity("arrivalHubAlias");

        try {
            List<HubRouteEntity> results = jpaQueryFactory.selectFrom(hubRouteEntity)
                .join(hubRouteEntity.departureHub, departureHubAlias).fetchJoin()
                .join(hubRouteEntity.arrivalHub, arrivalHubAlias).fetchJoin()
                .where(isNotDeleted, isActive, matchesHubId)
                .fetch();

            log.info("[허브 경로 조회] 결과 개수: {}", results.size());

            return results;
        } catch (Exception e) {
            log.error("[허브 경로 조회] 쿼리 실행 중 예외 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.HUB_ROUTE_BAD_REQUEST);
        }
    }

    /*
    select hr.*
    from hub_route_entity hr
    where hr.is_deleted = false
      and (hr.departure_hub_id = :hubId or hr.arrival_hub_id = :hubId)
      and (
          lower(hr.departure_hub_name) like concat('%', lower(:keyword), '%') or
          lower(hr.departure_hub_address_sido) like concat('%', lower(:keyword), '%') or
          lower(hr.departure_hub_address_sigungu) like concat('%', lower(:keyword), '%') or
          lower(hr.departure_hub_address_eupmyun) like concat('%', lower(:keyword), '%') or
          lower(hr.departure_hub_address_road_name) like concat('%', lower(:keyword), '%') or
          lower(hr.departure_hub_address_building_number) like concat('%', lower(:keyword), '%') or
          lower(hr.departure_hub_address_zip_code) like concat('%', lower(:keyword), '%')
      )
    order by hr.created_at desc
    limit :limit offset :offset;

    select count(*)
    from hub_route_entity hr
    where hr.is_deleted = false
      and (hr.departure_hub_id = :hubId
           or hr.arrival_hub_id = :hubId)
      and (:keyword is null
           or lower(hr.keyword) like concat('%', lower(:keyword), '%'));
    */
    @Override
    public Page<HubRouteEntity> findAllHubRoutesByHubIdAndIsDeletedFalseWithKeyword(
        String hubId,
        String keyword,
        Pageable pageable
    ) {

        BooleanBuilder builder = buildKeywordSearchConditions(hubId, keyword);

        List<OrderSpecifier<?>> orderSpecifiers = pageable.getSort().stream()
            .map(order -> mapToOrderSpecifier(order.getProperty(), order.isAscending()))
            .collect(Collectors.toList());

        List<HubRouteEntity> results = jpaQueryFactory.selectFrom(hubRouteEntity)
            .join(hubRouteEntity.departureHub).fetchJoin()
            .join(hubRouteEntity.arrivalHub).fetchJoin()
            .where(builder)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCount = jpaQueryFactory.select(hubRouteEntity.count())
            .from(hubRouteEntity)
            .where(builder)
            .fetchOne();

        long total = (totalCount != null) ? totalCount : 0L;

        return new PageImpl<>(results, pageable, total);
    }

    /*
    select
        hr.*
    from
        hub_route_entity hr
    join
        hub_entity h1 on hr.departure_hub_id = h1.hub_id
    join
        hub_entity h2 on hr.arrival_hub_id = h2.hub_id
    where
        hr.is_deleted = false
        and (h1.hub_id = :hubId or h2.hub_id = :hubId)
    */
    @Override
    public List<HubRouteEntity> findAllHubRoutesByHubIdAndIsDeletedFalse(String hubId) {

        QHubEntity departureHub =  new QHubEntity("departureHub");
        QHubEntity arrivalHub = new QHubEntity("arrivalHub");

        BooleanExpression isHubRouteNotDeleted = hubRouteEntity.isDeleted.isFalse();
        BooleanExpression isDepartureHub = hubRouteEntity.departureHub.hubId.eq(hubId);
        BooleanExpression isArrivalHub = hubRouteEntity.arrivalHub.hubId.eq(hubId);

        return jpaQueryFactory.selectFrom(hubRouteEntity)
            .join(hubRouteEntity.departureHub, departureHub) // departureHub와 명시적 조인
            .join(hubRouteEntity.arrivalHub, arrivalHub)
            .where(
                isHubRouteNotDeleted.and(isDepartureHub.or(isArrivalHub))
            )
            .fetch();
    }

    private BooleanBuilder buildKeywordSearchConditions(String hubId, String keyword) {

        BooleanExpression isHubIdEquals = hubRouteEntity.departureHub.hubId.eq(hubId);
        BooleanExpression isDeleted = hubRouteEntity.isDeleted.eq(false);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(isDeleted);
        builder.and(isHubIdEquals);

        if (keyword != null && !keyword.isBlank()) {
            String trimmedKeyword = keyword.trim();

            // 여러 필드에 대한 검색 조건 추가
            builder.and(
                hubRouteEntity.arrivalHub.hubName.containsIgnoreCase(trimmedKeyword)
                    .or(hubRouteEntity.arrivalHub.hubAddress.sido.containsIgnoreCase(trimmedKeyword))
                    .or(hubRouteEntity.arrivalHub.hubAddress.sigungu.containsIgnoreCase(trimmedKeyword))
                    .or(hubRouteEntity.arrivalHub.hubAddress.eupmyun.containsIgnoreCase(trimmedKeyword))
                    .or(hubRouteEntity.arrivalHub.hubAddress.roadName.containsIgnoreCase(trimmedKeyword))
                    .or(hubRouteEntity.arrivalHub.hubAddress.buildingNumber.containsIgnoreCase(trimmedKeyword))
                    .or(hubRouteEntity.arrivalHub.hubAddress.zipCode.containsIgnoreCase(trimmedKeyword))
            );
        }

        return builder;
    }

    private OrderSpecifier<?> mapToOrderSpecifier(String sortProperty, boolean ascending) {

        log.info("[HubRoute QueryDSL] Applying sort: {} ascending: {}", sortProperty, ascending);

        return switch (sortProperty) {
            case "createdAt" -> ascending ? hubRouteEntity.createdAt.asc() : hubRouteEntity.createdAt.desc();
            case "updatedAt" -> ascending ? hubRouteEntity.updatedAt.asc() : hubRouteEntity.updatedAt.desc();
            default -> hubRouteEntity.createdAt.desc();
        };
    }
}
