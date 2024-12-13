package com.sparta.blackyolk.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "logistic-service", url = "http://localhost:19092") // logistic-service URL로 설정
@FeignClient(name = "logistic-service")
public interface LogisticServiceClient {

    @GetMapping("/api/hubs/{hubId}") // 허브 ID로 조회하는 엔드포인트
    Object getHubById(@PathVariable("hubId") String hubId);
}
