package com.sparta.blackyolk.auth_service.courier.controller;

import com.sparta.blackyolk.auth_service.courier.dto.CourierRequestDto;
import com.sparta.blackyolk.auth_service.courier.dto.CourierResponseDto;
import com.sparta.blackyolk.auth_service.courier.dto.CourierUpdateRequestDto;
import com.sparta.blackyolk.auth_service.courier.service.CourierService;
import com.sparta.blackyolk.auth_service.security.UserDetailsImpl;
import com.sparta.blackyolk.auth_service.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;
    private final UserDetailsImpl userDetails;

    // 배송 담당자 등록
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_ADMIN')")
    @PostMapping
    public CourierResponseDto createCourier(
            @RequestBody CourierRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Registering a courier. Role: {}", userDetails.getUser().getRole());
        return courierService.createCourier(requestDto, userDetails.getUser());
    }

    // 특정 배송 담당자 정보 조회 (마스터, 허브 관리자, 배송 담당자 본인)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_ADMIN', 'HUB_DELIVERY', 'COMPANY_DELIVERY')")
    @GetMapping("/{courierId}")
    public CourierResponseDto getCourier(
            @PathVariable UUID courierId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Fetching courier details for ID: {}", courierId);
        return courierService.getCourier(courierId, userDetails.getUser());
    }

    // 배송 담당자 정보 조회 (특정 허브 또는 전체, 마스터와 허브 관리자만 가능)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_ADMIN')")
    @GetMapping
    public List<CourierResponseDto> getCouriers(
            @RequestParam(required = false) UUID hubId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Fetching couriers for hubId: {}", hubId);
        return courierService.getCouriers(hubId, userDetails.getUser());
    }

    // 배송 담당자 수정 (마스터와 허브 관리자만 가능)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_ADMIN')")
    @PutMapping("/{courierId}")
    public CourierResponseDto updateCourier(
            @PathVariable UUID courierId,
            @RequestBody CourierUpdateRequestDto updateRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Updating courier ID: {}", courierId);
        return courierService.updateCourier(courierId, updateRequestDto, userDetails.getUser());
    }

    // 배송 담당자 삭제 (마스터와 허브 관리자만 가능)
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_ADMIN')")
    @DeleteMapping("/{courierId}")
    public CourierResponseDto deleteCourier(
            @PathVariable UUID courierId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Deleting courier ID: {}", courierId);
        return courierService.deleteCourier(courierId, userDetails.getUser());
    }

    // 본인 정보 조회 (배송 담당자만 가능)
    @PreAuthorize("hasAnyRole('HUB_DELIVERY', 'COMPANY_DELIVERY')")
    @GetMapping("/me")
    public CourierResponseDto getMyCourierDetails(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("Fetching courier details for user: {}", userDetails.getUsername());
        return courierService.getMyCourierDetails(userDetails.getUser());
    }
}
