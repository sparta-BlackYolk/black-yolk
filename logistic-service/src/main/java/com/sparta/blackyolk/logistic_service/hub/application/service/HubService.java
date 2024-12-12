package com.sparta.blackyolk.logistic_service.hub.application.service;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForDelete;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForUpdate;
import com.sparta.blackyolk.logistic_service.hub.application.port.HubPersistencePort;
import com.sparta.blackyolk.logistic_service.hub.application.usecase.HubUseCase;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubService implements HubUseCase {

    private final HubPersistencePort hubPersistencePort;
    private final String URL = "https://dapi.kakao.com/v2/local/search/address.json?query=";
    private final String ADDRESS = "제주 애월읍";

    @Override
    public Hub createHub(HubForCreate hubForCreate) {

        // TODO : 사용자 권한 예외처리 추가 & hubManagerId 있으면 hubManagerId 검증하는 로직 필요

        // TODO: 좌표 조회하는 로직
        String url = URL + ADDRESS;

        BigDecimal axisX = new BigDecimal("126.851675");
        BigDecimal axisY = new BigDecimal("37.54815556");

        // TODO : 허브 이름이나 좌표 혹은 주소 가지고 중복된 허브인지 확인하는 로직 추가

        return hubPersistencePort.saveHub(hubForCreate, axisX, axisY);
    }

    @Override
    public Hub updateHub(HubForUpdate hubForUpdate) {

        // TODO : 사용자 권한 예외처리 추가 & hubManagerId 있으면 hubManagerId 검증하는 로직 필요

        // TODO : 예외 처리 하기
        Hub hub = hubPersistencePort.findByHubId(hubForUpdate.hubId()).orElseThrow(

        );

        BigDecimal axisX = hub.getHubCoordinate().getAxisX();
        BigDecimal axisY = hub.getHubCoordinate().getAxisY();

        // TODO : 좌표 업데이트 하는 로직 추가
        if (hubForUpdate.address() != null) {
            // 좌표 업데이트 함
            axisX = new BigDecimal("130.851675");
            axisY = new BigDecimal("40.54815556");
        }

        return hubPersistencePort.updateHub(hubForUpdate, axisX, axisY);
    }

    @Override
    public Hub deleteHub(HubForDelete hubForDelete) {

        // TODO : 사용자 권한 예외처리 추가

        // TODO : 예외 처리 하기
        Hub hub = hubPersistencePort.findByHubId(hubForDelete.hubId()).orElseThrow(

        );

        return hubPersistencePort.deleteHub(hubForDelete);
    }
}
