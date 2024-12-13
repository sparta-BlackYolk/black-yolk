package com.sparta.blackyolk.auth_service.courier.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CourierUpdateRequestDto {
    private String slackId; // 슬랙 아이디
    private UUID hubId; // 허브 ID
}
