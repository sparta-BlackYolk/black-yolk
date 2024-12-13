package com.sparta.blackyolk.auth_service.courier.controller;

import com.sparta.blackyolk.auth_service.courier.dto.CourierRequestDto;
import com.sparta.blackyolk.auth_service.courier.dto.CourierResponseDto;
import com.sparta.blackyolk.auth_service.courier.dto.CourierUpdateRequestDto;
import com.sparta.blackyolk.auth_service.courier.service.CourierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    // 배송 담당자 등록
    @PostMapping
    public CourierResponseDto createCourier(@RequestBody CourierRequestDto requestDto) {
        return courierService.createCourier(requestDto);
    }

    // 특정 배송 담당자 정보 조회
    @GetMapping("/{courierId}")
    public CourierResponseDto getCourier(@PathVariable UUID courierId) {
        return courierService.getCourier(courierId);
    }

    // 배송 담당자 정보 조회 (특정 허브 또는 전체)
    @GetMapping
    public List<CourierResponseDto> getCouriers(@RequestParam(required = false) UUID hubId) {
        return courierService.getCouriers(hubId);
    }


    // 배송 담당자 수정
    @PutMapping("/{courierId}")
    public CourierResponseDto updateCourier(
            @PathVariable UUID courierId,
            @RequestBody CourierUpdateRequestDto updateRequestDto) {
        return courierService.updateCourier(courierId, updateRequestDto);
    }

    // 배송 담당자 삭제
    @DeleteMapping("/{courierId}")
    public CourierResponseDto deleteCourier(@PathVariable UUID courierId) {
        return courierService.deleteCourier(courierId);
    }
}
