package com.sparta.msa_exam.order.order_service.application.dto.request;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public record OrderSearchRequestDto(
        int page,
        int limit,
        String searchValue,
        String searchType,         // 검색 유형 ORDER_ID, REQUESTER_NAME, SUPPLIER_NAME
        String orderBy,
        Sort.Direction sort )
{
    public OrderSearchRequestDto() {
        this(1, 10, null, null, "createdAt", Sort.Direction.DESC);
    }

    public Pageable getPageable() {
        return PageRequest.of(page - 1, limit, sort, orderBy);
    }
}
