package com.sparta.blackyolk.auth_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomPageResponse<T> {
    private List<T> content; // 페이징된 데이터
    private int page;        // 현재 페이지 번호
    private int size;        // 페이지 크기
    private String sort;     // 정렬 정보
}
