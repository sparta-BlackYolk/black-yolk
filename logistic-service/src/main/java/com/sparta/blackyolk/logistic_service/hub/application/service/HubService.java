package com.sparta.blackyolk.logistic_service.hub.application.service;

import com.sparta.blackyolk.logistic_service.common.domain.UserResponseDto;
import com.sparta.blackyolk.logistic_service.common.domain.vo.UserRoleEnum;
import com.sparta.blackyolk.logistic_service.common.exception.CustomException;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.common.service.UserService;
import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForDelete;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForUpdate;
import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubCoordinate;
import com.sparta.blackyolk.logistic_service.hub.application.port.HubPersistencePort;
import com.sparta.blackyolk.logistic_service.hub.application.usecase.HubUseCase;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubCreateResponse;
import com.sparta.blackyolk.logistic_service.hub.framework.web.dto.HubUpdateResponse;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubService implements HubUseCase {

    private final HubPersistencePort hubPersistencePort;
    private final HubCacheService hubCacheService;
    private final CoordinateService coordinateService;
    private final UserService userService;

    @Override
    public HubCreateResponse createHub(HubForCreate hubForCreate) {

        if (hubForCreate.hubManagerId() != null) {
            validateHubManagerId(hubForCreate.hubManagerId(), hubForCreate.authorization());
        }

        validateMaster(hubForCreate.role());
        validateHubCenter(hubForCreate.center());

        String hubAddress = getHubAddress(
            hubForCreate.sido(),
            hubForCreate.sigungu(),
            hubForCreate.eupmyun(),
            hubForCreate.roadName(),
            hubForCreate.buildingNumber()
        );
        HubCoordinate coordinates = coordinateService.getCoordinatesByAddress(hubAddress);

        return hubCacheService.createHub(hubForCreate, coordinates.getAxisX(), coordinates.getAxisY());
    }

    // TODO: 불필요한 쿼리가 날아가지 않는가? 확인
    @Override
    public HubUpdateResponse updateHub(HubForUpdate hubForUpdate) {

        if (hubForUpdate.hubManagerId() != null) {
            validateHubManagerId(hubForUpdate.hubManagerId(), hubForUpdate.authorization());
        }

        validateMaster(hubForUpdate.role());

        Hub hub = hubCacheService.validateHub(hubForUpdate.hubId());

        BigDecimal axisX = hub.getHubCoordinate().getAxisX();
        BigDecimal axisY = hub.getHubCoordinate().getAxisY();

        if (hubForUpdate.address() != null) {
            String hubAddress = getHubAddress(
                hubForUpdate.address().sido(),
                hubForUpdate.address().sigungu(),
                hubForUpdate.address().eupmyun(),
                hubForUpdate.address().roadName(),
                hubForUpdate.address().buildingNumber()
            );
            HubCoordinate coordinates = coordinateService.getCoordinatesByAddress(hubAddress);
            axisX = coordinates.getAxisX();
            axisY = coordinates.getAxisY();
        }

        return hubCacheService.updateHub(hubForUpdate, axisX, axisY);
    }

    // TODO: 불필요한 쿼리가 날아가지 않는가? 확인
    @Override
    public Hub deleteHub(HubForDelete hubForDelete) {

        validateMaster(hubForDelete.role());
        hubCacheService.validateHub(hubForDelete.hubId());

        return hubCacheService.deleteHub(hubForDelete);
    }

    public Hub validateHubWithHubRoutes(String hubId) {
        return hubPersistencePort.findByHubIdWithHubRoutes(hubId).orElseThrow(
            () -> new CustomException(ErrorCode.HUB_NOT_EXIST)
        );
    }

    private void validateMaster(String role) {
        if (!"MASTER".equals(role)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    private void validateHubManagerId(String hubManagerId, String authorization) {

        log.info("[Hub 생성] authorization 확인: {}", authorization);

        UserResponseDto user = userService.getUser(hubManagerId, authorization).orElseThrow(
            () -> new CustomException(ErrorCode.USER_NOT_EXIST)
        );

        if (!user.getRole().equals(UserRoleEnum.HUB_ADMIN)) {
            log.info("[Hub 생성] user role 확인: {}", user.getRole());
            throw new CustomException(ErrorCode.USER_BAD_REQUEST, "HUB_ADMIN 권한의 사용자가 아닙니다.");
        }
    }

    private void validateHubCenter(String center) {
        Optional<Hub> hub = hubPersistencePort.findByHubCenter(center);
        if (hub.isPresent()) {
            throw new CustomException(ErrorCode.HUB_ALREADY_EXIST);
        }
    }

    private String getHubAddress(
        String sido,
        String sigungu,
        String eupmyun,
        String roadName,
        String buildingNumber
    ) {
        return Stream.of(sido, sigungu, eupmyun, roadName, buildingNumber)
                 .filter(Objects::nonNull) // null 필드 제거
                 .filter(s -> !s.isBlank()) // 빈 문자열 제거
                 .collect(Collectors.joining(" "));
    }
}
