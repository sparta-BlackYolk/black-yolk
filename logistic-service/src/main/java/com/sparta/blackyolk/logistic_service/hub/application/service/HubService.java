package com.sparta.blackyolk.logistic_service.hub.application.service;

import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.domain.HubForCreate;
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
}
