package com.sparta.blackyolk.auth_service.courier.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class CourierRequestDto {
    private Long userId;
    private UUID hubId;
}
