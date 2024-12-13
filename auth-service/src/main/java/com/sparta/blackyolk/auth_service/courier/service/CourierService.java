package com.sparta.blackyolk.auth_service.courier.service;

import com.sparta.blackyolk.auth_service.client.LogisticServiceClient;
import com.sparta.blackyolk.auth_service.courier.dto.CourierRequestDto;
import com.sparta.blackyolk.auth_service.courier.dto.CourierResponseDto;
import com.sparta.blackyolk.auth_service.courier.dto.CourierUpdateRequestDto;
import com.sparta.blackyolk.auth_service.courier.entity.Courier;
import com.sparta.blackyolk.auth_service.courier.repository.CourierRepository;
import com.sparta.blackyolk.auth_service.user.entity.User;
import com.sparta.blackyolk.auth_service.user.entity.UserRoleEnum;
import com.sparta.blackyolk.auth_service.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;
    private final UserRepository userRepository;
    private final LogisticServiceClient logisticServiceClient;

    // 배송 담당자 등록
    @Transactional
    public CourierResponseDto createCourier(CourierRequestDto requestDto) {
        // 1. userId를 기반으로 사용자 정보 조회
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. userId: " + requestDto.getUserId()));

        // 2. 허브 ID 검증 (업체 배송 담당자일 경우)
        if (user.getRole() == UserRoleEnum.COMPANY_DELIVERY) {
            if (requestDto.getHubId() == null) {
                throw new IllegalArgumentException("COMPANY_DELIVERY 역할에는 hubId가 필수입니다.");
            }

            // FeignClient를 통해 logistic-service에서 hubId 검증
            try {
                logisticServiceClient.getHubById(requestDto.getHubId().toString());
            } catch (Exception e) {
                throw new IllegalArgumentException("유효하지 않은 hubId입니다: " + requestDto.getHubId());
            }
        }

        // 3. 전체 순번 조회 및 새로운 순번 생성
        Optional<Integer> maxDeliveryNum = courierRepository.findMaxDeliveryNumWithLock();
        String newDeliveryNum = String.format("%03d", maxDeliveryNum.orElse(0) + 1);

        // 4. 배송 담당자 생성
        Courier courier = Courier.builder()
                .userId(requestDto.getUserId())
                .hubId(requestDto.getHubId()) // null일 수도 있음 (HUB_DELIVERY일 경우)
                .deliveryNum(newDeliveryNum)
                .slackId(user.getSlackId()) // user 테이블에서 조회한 slackId
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();

        // 5. 배송 담당자 저장
        courierRepository.save(courier);

        // 6. 응답 DTO 생성 및 반환
        return CourierResponseDto.builder()
                .courierId(courier.getCourierId())
                .userId(courier.getUserId())
                .hubId(courier.getHubId())
                .deliveryNum(courier.getDeliveryNum())
                .createdAt(courier.getCreatedAt())
                .build();
    }





    // 배송 담당자 정보 조회
    public CourierResponseDto getCourier(UUID courierId) {
        Courier courier = courierRepository.findByCourierId(courierId)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found"));

        return CourierResponseDto.builder()
                .courierId(courier.getCourierId())
                .userId(courier.getUserId())
                .hubId(courier.getHubId())
                .deliveryNum(courier.getDeliveryNum())
                .createdAt(courier.getCreatedAt())
                .build();
    }

    // 특정 허브의 배송 담당자 조회 또는 전체 조회
    @Transactional
    public List<CourierResponseDto> getCouriers(UUID hubId) {
        List<Courier> couriers;

        if (hubId != null) {
            couriers = courierRepository.findAllByHubId(hubId);
        } else {
            couriers = courierRepository.findAll();
        }

        // Entity -> DTO 변환
        return couriers.stream()
                .map(courier -> CourierResponseDto.builder()
                        .courierId(courier.getCourierId())
                        .userId(courier.getUserId())
                        .hubId(courier.getHubId())
                        .deliveryNum(courier.getDeliveryNum())
                        .createdAt(courier.getCreatedAt())
                        .build())
                .toList();
    }


    // 배송 담당자 수정
    // 배송 담당자 수정
    @Transactional
    public CourierResponseDto updateCourier(UUID courierId, CourierUpdateRequestDto updateRequestDto) {
        // 1. 배송 담당자 정보 조회
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배송 담당자를 찾을 수 없습니다. courierId: " + courierId));

        // 2. 사용자 정보 조회
        User user = userRepository.findById(courier.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자 정보를 찾을 수 없습니다. userId: " + courier.getUserId()));

        // 3. 역할에 따라 허브 ID 검증
        if (user.getRole() == UserRoleEnum.COMPANY_DELIVERY) {
            if (updateRequestDto.getHubId() != null) {
                // FeignClient를 통해 logistic-service에서 hubId 검증
                try {
                    logisticServiceClient.getHubById(updateRequestDto.getHubId().toString());
                } catch (Exception e) {
                    throw new IllegalArgumentException("유효하지 않은 hubId입니다: " + updateRequestDto.getHubId());
                }
                courier.setHubId(updateRequestDto.getHubId());
            } else {
                throw new IllegalArgumentException("COMPANY_DELIVERY 역할에는 hubId가 필수입니다.");
            }
        }

        // 4. 슬랙 아이디 업데이트
        if (updateRequestDto.getSlackId() != null) {
            courier.setSlackId(updateRequestDto.getSlackId());
        }

        // 5. 수정된 배송 담당자 저장
        courierRepository.save(courier);

        // 6. 응답 DTO 생성 및 반환
        return CourierResponseDto.builder()
                .courierId(courier.getCourierId())
                .userId(courier.getUserId())
                .hubId(courier.getHubId())
                .deliveryNum(courier.getDeliveryNum())
                .slackId(courier.getSlackId())
                .updatedAt(courier.getUpdatedAt())
                .build();
    }


    // 배송 담당자 삭제
    public CourierResponseDto deleteCourier(UUID courierId) {
        Courier courier = courierRepository.findByCourierId(courierId)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found"));

        courier.setIsDeleted(true);
        courier.setDeletedAt(LocalDateTime.now());
        Courier deletedCourier = courierRepository.save(courier);

        return CourierResponseDto.builder()
                .courierId(deletedCourier.getCourierId())
                .deletedAt(deletedCourier.getDeletedAt())
                .build();
    }
}
