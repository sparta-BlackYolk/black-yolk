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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;
    private final UserRepository userRepository;
    private final LogisticServiceClient logisticServiceClient;

    // 배송 담당자 등록
    @Transactional
    public CourierResponseDto createCourier(CourierRequestDto requestDto, User loggedInUser) {
        // 1. 등록하려는 사용자의 정보를 조회
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. userId: " + requestDto.getUserId()));

        // 2. 배송 담당자 중복 여부 검증
        if (courierRepository.existsByUserId(requestDto.getUserId())) {
            throw new IllegalArgumentException("해당 사용자(userId: " + requestDto.getUserId() + ")는 이미 배송 담당자로 등록되어 있습니다.");
        }

        // 3. 역할에 따른 로직 분기
        if (loggedInUser.getRole() == UserRoleEnum.MASTER) {
            // 마스터는 모든 권한을 가지고 바로 저장 가능
            return saveCourier(requestDto, user);
        } else if (loggedInUser.getRole() == UserRoleEnum.HUB_ADMIN) {
            // 허브 관리자는 자신의 허브에 속한 배송 담당자만 등록 가능
            validateHubOwnership(loggedInUser, requestDto.getHubId());
            return saveCourier(requestDto, user);
        } else {
            // 그 외의 역할은 등록 권한 없음
            throw new IllegalArgumentException("배송 담당자를 등록할 권한이 없습니다.");
        }
    }

    // 배송 담당자 저장 로직
    private CourierResponseDto saveCourier(CourierRequestDto requestDto, User user) {
        // 1. 새로운 순번 생성
        Optional<Integer> maxDeliveryNum = courierRepository.findMaxDeliveryNumWithLock();
        String newDeliveryNum = String.format("%03d", maxDeliveryNum.orElse(0) + 1);

        // 2. 배송 담당자 생성
        Courier courier = Courier.builder()
                .userId(requestDto.getUserId())
                .hubId(requestDto.getHubId()) // null일 수도 있음 (HUB_DELIVERY일 경우)
                .deliveryNum(newDeliveryNum)
                .slackId(user.getSlackId()) // user 테이블에서 조회한 slackId
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();

        // 3. 배송 담당자 저장
        courierRepository.save(courier);

        // 4. 응답 DTO 생성 및 반환
        return CourierResponseDto.builder()
                .courierId(courier.getCourierId())
                .userId(courier.getUserId())
                .hubId(courier.getHubId())
                .deliveryNum(courier.getDeliveryNum())
                .createdAt(courier.getCreatedAt())
                .build();
    }

    // 특정 배송 담당자 정보 조회
    public CourierResponseDto getCourier(UUID courierId, User loggedInUser) {
        Courier courier = courierRepository.findByCourierId(courierId)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found"));

        // 권한 확인
        if (loggedInUser.getRole() == UserRoleEnum.MASTER) {
            // 마스터는 모든 정보 조회 가능
            return convertToResponseDto(courier);
        } else if (loggedInUser.getRole() == UserRoleEnum.HUB_ADMIN) {
            // 허브 관리자일 경우, 허브 소유권 확인
            validateHubOwnership(loggedInUser, courier.getHubId());
            return convertToResponseDto(courier);
        } else if (loggedInUser.getRole() == UserRoleEnum.HUB_DELIVERY || loggedInUser.getRole() == UserRoleEnum.COMPANY_DELIVERY) {
            // 배송 담당자는 본인 정보만 조회 가능
            if (!loggedInUser.getId().equals(courier.getUserId())) {
                throw new IllegalArgumentException("권한이 없습니다.");
            }
            return convertToResponseDto(courier);
        } else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    // 특정 허브 또는 전체 배송 담당자 조회
    public List<CourierResponseDto> getCouriers(UUID hubId, User loggedInUser) {
        if (loggedInUser.getRole() == UserRoleEnum.MASTER) {
            // 마스터는 전체 조회 가능
            return convertToResponseDtos(
                    hubId != null ? courierRepository.findAllByHubId(hubId) : courierRepository.findAll()
            );
        } else if (loggedInUser.getRole() == UserRoleEnum.HUB_ADMIN) {
            // 허브 관리자는 자신의 허브에 속한 배송 담당자만 조회 가능
            validateHubOwnership(loggedInUser, hubId);
            return convertToResponseDtos(courierRepository.findAllByHubId(hubId));
        } else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    // 배송 담당자 수정
    @Transactional
    public CourierResponseDto updateCourier(UUID courierId, CourierUpdateRequestDto updateRequestDto, User loggedInUser) {
        // 1. 배송 담당자 정보 조회
        Courier courier = courierRepository.findByCourierId(courierId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배송 담당자를 찾을 수 없습니다. courierId: " + courierId));

        // 2. 권한에 따른 처리
        if (loggedInUser.getRole() == UserRoleEnum.MASTER) {
            // MASTER는 모든 정보 수정 가능
            return applyUpdates(courier, updateRequestDto);

        } else if (loggedInUser.getRole() == UserRoleEnum.HUB_ADMIN) {
            // HUB_ADMIN은 자신이 관리하는 허브 소속의 배송 담당자만 수정 가능
            validateHubOwnership(loggedInUser, courier.getHubId());
            return applyUpdates(courier, updateRequestDto);

        } else {
            // 다른 역할은 수정 권한 없음
            throw new IllegalArgumentException("권한이 없습니다. 수정할 수 없습니다.");
        }
    }

    // 공통: 수정 로직 처리
    private CourierResponseDto applyUpdates(Courier courier, CourierUpdateRequestDto updateRequestDto) {
        // 허브 ID 수정
        if (updateRequestDto.getHubId() != null) {
            try {
                // 허브 ID 검증
                logisticServiceClient.getHubById(updateRequestDto.getHubId().toString());
            } catch (Exception e) {
                throw new IllegalArgumentException("유효하지 않은 hubId입니다: " + updateRequestDto.getHubId());
            }
            courier.setHubId(updateRequestDto.getHubId());
        }

        // Slack ID 수정
        if (updateRequestDto.getSlackId() != null) {
            courier.setSlackId(updateRequestDto.getSlackId());
        }

        // 변경 사항 저장
        courierRepository.save(courier);

        // 응답 DTO 반환
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
    @Transactional
    public CourierResponseDto deleteCourier(UUID courierId, User loggedInUser) {
        Courier courier = courierRepository.findByCourierId(courierId)
                .orElseThrow(() -> new IllegalArgumentException("Courier not found"));

        if (loggedInUser.getRole() == UserRoleEnum.MASTER) {
            // 마스터는 삭제 가능
            return deleteCourierDetails(courier);
        } else if (loggedInUser.getRole() == UserRoleEnum.HUB_ADMIN) {
            // 허브 관리자는 자신의 허브에 속한 배송 담당자만 삭제 가능
            validateHubOwnership(loggedInUser, courier.getHubId());
            return deleteCourierDetails(courier);
        } else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    // 본인 정보 조회
    public CourierResponseDto getMyCourierDetails(User loggedInUser) {
        Courier courier = courierRepository.findByUserId(loggedInUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("본인의 배송 담당자 정보를 찾을 수 없습니다."));
        return convertToResponseDto(courier);
    }

    // 공통: 허브 소유권 검증 로직
    private void validateHubOwnership(User hubAdmin, UUID hubId) {
        if (hubId == null) {
            throw new IllegalArgumentException("허브 ID가 필요합니다.");
        }

        boolean isOwner = logisticServiceClient.isHubAdmin(hubId.toString(), hubAdmin.getId());
        if (!isOwner) {
            throw new IllegalArgumentException("권한이 없습니다. 해당 허브에 속한 배송 담당자가 아닙니다.");
        }
    }

    // 공통: 응답 DTO 변환
    private CourierResponseDto convertToResponseDto(Courier courier) {
        return CourierResponseDto.builder()
                .courierId(courier.getCourierId())
                .userId(courier.getUserId())
                .hubId(courier.getHubId())
                .deliveryNum(courier.getDeliveryNum())
                .createdAt(courier.getCreatedAt())
                .build();
    }

    private List<CourierResponseDto> convertToResponseDtos(List<Courier> couriers) {
        return couriers.stream().map(this::convertToResponseDto).toList();
    }

    // 공통: 수정 로직
    private CourierResponseDto updateCourierDetails(Courier courier, CourierUpdateRequestDto updateRequestDto) {
        if (updateRequestDto.getSlackId() != null) {
            courier.setSlackId(updateRequestDto.getSlackId());
        }
        courierRepository.save(courier);
        return convertToResponseDto(courier);
    }

    // 공통: 삭제 로직
    private CourierResponseDto deleteCourierDetails(Courier courier) {
        courier.setIsDeleted(true);
        courier.setDeletedAt(LocalDateTime.now());
        courierRepository.save(courier);
        return convertToResponseDto(courier);
    }
}
