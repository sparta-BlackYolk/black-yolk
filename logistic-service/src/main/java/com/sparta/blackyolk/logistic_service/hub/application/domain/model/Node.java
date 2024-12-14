package com.sparta.blackyolk.logistic_service.hub.application.domain.model;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Node {
    private final Hub hub; // 현재 탐색 허브
    private final double g; // 출발 허브에서 현재 허브까지의 실제 비용
    private final double f; // g + 휴리스틱 비용, (f = g + h)
}
