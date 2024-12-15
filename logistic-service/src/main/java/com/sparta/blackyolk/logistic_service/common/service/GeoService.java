package com.sparta.blackyolk.logistic_service.common.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.blackyolk.logistic_service.common.exception.CustomException;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.hub.application.domain.model.HubCoordinate;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${naver.url}")
    private String API_URL;

    @Value("${naver.rest-api-key-id}")
    private String API_KEY_ID;

    @Value("${naver.rest-api-key}")
    private String API_KEY;

    public HubCoordinate getCoordinatesByAddress(String address) {

        log.info("[GeoService 좌표 조회] 주소 : {}", address);

        try {

            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);

            log.info("[GeoService 좌표 조회] 주소 : {}", encodedAddress);

            URI uri = new URI(API_URL + "?query=" + encodedAddress);

            log.info("[GeoService 좌표 조회] 요청 주소 : {}", uri);

            RequestEntity<Void> request = RequestEntity
                .get(uri)
                .header("X-NCP-APIGW-API-KEY-ID", API_KEY_ID)
                .header("X-NCP-APIGW-API-KEY", API_KEY)
                .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            return extractHubCoordinates(response.getBody());

        } catch (URISyntaxException e) {
            throw new CustomException(ErrorCode.URI_BAD_REQUEST);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REST_API_FAIL, e.getMessage());
        }
    }

    private HubCoordinate extractHubCoordinates(String body) {
        try {

            JsonNode rootNode = objectMapper.readTree(body);

            log.info("[GeoService 좌표 조회] JSON 내용 : {}", rootNode.toString());

            JsonNode addressNode = rootNode.path("addresses").get(0);

            String x = addressNode.path("x").asText();
            String y = addressNode.path("y").asText();

            return new HubCoordinate(new BigDecimal(x), new BigDecimal(y));

        } catch(Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 센터에 따른 좌표를 반환하는 메서드 - Test 용
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
