package com.sparta.blackyolk.auth_service.client;

import com.sparta.blackyolk.auth_service.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "logistic-service",configuration = FeignConfig.class)

public interface LogisticServiceClient {

    @GetMapping("/api/hubs/{hubId}/exists")
    boolean checkHubExists(
            @PathVariable("hubId") String hubId
    );


    @GetMapping("/api/hubs/isAdmin")
    boolean isHubAdmin(@RequestParam("hubId") String hubId,
                       @RequestParam("userName") String userName
    );


}
