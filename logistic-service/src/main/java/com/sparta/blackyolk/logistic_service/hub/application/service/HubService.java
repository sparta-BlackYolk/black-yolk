package com.sparta.blackyolk.logistic_service.hub.application.service;

import com.sparta.blackyolk.logistic_service.common.exception.CustomException;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubService implements HubUseCase {

    private final HubPersistencePort hubPersistencePort;
    private final HubCacheService hubCacheService;

    private final String URL = "https://dapi.kakao.com/v2/local/search/address.json?query=";
    private final String ADDRESS = "제주 애월읍";

    @Override
    public HubCreateResponse createHub(HubForCreate hubForCreate) {

        // TODO : hubManagerId 있으면 hubManagerId 검증하는 로직 필요
        validateMaster(hubForCreate.role());
        validateHubCenter(hubForCreate.center());

        // TODO: 추후에 좌표 조회하는 로직으로 변경 하기
        HubCoordinate coordinates = getCoordinatesByCenter(hubForCreate.center());

        return hubCacheService.createHub(hubForCreate, coordinates.getAxisX(), coordinates.getAxisY());
    }

    // TODO: 불필요한 쿼리가 날아가지 않는가? 확인
    @Override
    public HubUpdateResponse updateHub(HubForUpdate hubForUpdate) {

        // TODO : hubManagerId 있으면 hubManagerId 검증하는 로직 필요
        validateMaster(hubForUpdate.role());

        Hub hub = hubCacheService.validateHub(hubForUpdate.hubId());

        BigDecimal axisX = hub.getHubCoordinate().getAxisX();
        BigDecimal axisY = hub.getHubCoordinate().getAxisY();

        // TODO : 좌표 업데이트 하는 로직 추가 -> domain으로 넘길 수 있을까?
        if (hubForUpdate.address() != null) {
            // 좌표 업데이트 함
            axisX = new BigDecimal("130.851675");
            axisY = new BigDecimal("40.54815556");
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

    private void validateHubCenter(String center) {
        Optional<Hub> hub = hubPersistencePort.findByHubCenter(center);
        if (hub.isPresent()) {
            throw new CustomException(ErrorCode.HUB_ALREADY_EXIST);
        }
    }

    // TODO : 추후에 외부 API 를 통한 좌표 조회로 변경 하기
    /**
     * 센터에 따른 좌표를 반환하는 메서드
     *
     * @param center 센터 이름
     * @return HubCoordinate 객체 (x, y 좌표)
     */
    private HubCoordinate getCoordinatesByCenter(String center) {
        return switch (center) {
            case "서울특별시 센터" -> new HubCoordinate(new BigDecimal("126.978388"), new BigDecimal("37.566610"));
            case "경기 북부 센터" -> new HubCoordinate(new BigDecimal("127.061509"), new BigDecimal("37.895376"));
            case "경기 남부 센터" -> new HubCoordinate(new BigDecimal("127.123789"), new BigDecimal("37.263573"));
            case "부산광역시 센터" -> new HubCoordinate(new BigDecimal("129.075642"), new BigDecimal("35.179554"));
            case "대구광역시 센터" -> new HubCoordinate(new BigDecimal("128.601445"), new BigDecimal("35.871390"));
            case "인천광역시 센터" -> new HubCoordinate(new BigDecimal("126.705204"), new BigDecimal("37.456256"));
            case "광주광역시 센터" -> new HubCoordinate(new BigDecimal("126.851338"), new BigDecimal("35.160023"));
            case "대전광역시 센터" -> new HubCoordinate(new BigDecimal("127.384548"), new BigDecimal("36.350461"));
            case "울산광역시 센터" -> new HubCoordinate(new BigDecimal("129.311299"), new BigDecimal("35.539777"));
            case "세종특별자치시 센터" -> new HubCoordinate(new BigDecimal("127.289101"), new BigDecimal("36.480086"));
            case "강원특별자치도 센터" -> new HubCoordinate(new BigDecimal("127.730594"), new BigDecimal("37.885374"));
            case "충청북도 센터" -> new HubCoordinate(new BigDecimal("127.491282"), new BigDecimal("36.635748"));
            case "충청남도 센터" -> new HubCoordinate(new BigDecimal("126.705180"), new BigDecimal("36.518374"));
            case "전북특별자치도 센터" -> new HubCoordinate(new BigDecimal("127.108759"), new BigDecimal("35.821058"));
            case "전라남도 센터" -> new HubCoordinate(new BigDecimal("126.462919"), new BigDecimal("34.816111"));
            case "경상북도 센터" -> new HubCoordinate(new BigDecimal("128.602750"), new BigDecimal("36.574493"));
            case "경상남도 센터" -> new HubCoordinate(new BigDecimal("128.259623"), new BigDecimal("35.460773"));
            default -> throw new CustomException(ErrorCode.HUB_BAD_REQUEST);
        };
    }
}
