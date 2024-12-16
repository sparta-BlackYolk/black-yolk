package com.sparta.blackyolk.logistic_service.company.infrastructure;

import com.sparta.blackyolk.logistic_service.company.application.UserService;
import com.sparta.blackyolk.logistic_service.company.application.dto.UserData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service")
public interface UserClient extends UserService {

    @GetMapping("/api/auth/users/search")
    UserData getUserByDynamicCondition(@RequestParam(required = false) Long userId,
                                       @RequestParam(required = false) String username,
                                       @RequestParam(required = false) String email,
                                       @RequestParam(required = false) String slackId);

    @GetMapping("/{userId}")
    UserData getUserById(@PathVariable Long userId);
}
