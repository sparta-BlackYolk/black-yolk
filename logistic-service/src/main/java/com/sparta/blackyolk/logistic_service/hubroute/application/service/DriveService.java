package com.sparta.blackyolk.logistic_service.hubroute.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.blackyolk.logistic_service.common.exception.CustomException;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hubroute.application.domain.DriveInfo;
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
public class DriveService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${naver.drive-url}")
    private String DRIVE_API_URL;

    @Value("${naver.rest-api-key-id}")
    private String API_KEY_ID;

    @Value("${naver.rest-api-key}")
    private String API_KEY;

    public DriveInfo getDriveInfo(Hub departureHub, Hub arrivalHub) {

        log.info("[DriveService 좌표 조회] 출발지 : {}, 목적지 : {}", departureHub.getHubName(), arrivalHub.getHubName());

        try {

            String departureCoordinates = departureHub.getHubCoordinate().getAxisX()+","+departureHub.getHubCoordinate().getAxisY();
            String arrivalCoordinates = arrivalHub.getHubCoordinate().getAxisX()+","+arrivalHub.getHubCoordinate().getAxisY();

            log.info("[DriveService 좌표 조회] 출발지 좌표 : {}", departureCoordinates);
            log.info("[DriveService 좌표 조회] 목적지 좌표 : {}", arrivalCoordinates);

            String encodedDepartureCoordinates = URLEncoder.encode(departureCoordinates, StandardCharsets.UTF_8);
            String encodedArrivalCoordinates = URLEncoder.encode(arrivalCoordinates, StandardCharsets.UTF_8);

            URI uri = new URI(DRIVE_API_URL + "?goal=" + encodedDepartureCoordinates + "&start=" + encodedArrivalCoordinates);

            log.info("[DriveService 좌표 조회] 요청 주소 : {}", uri);

            RequestEntity<Void> request = RequestEntity
                .get(uri)
                .header("X-NCP-APIGW-API-KEY-ID", API_KEY_ID)
                .header("X-NCP-APIGW-API-KEY", API_KEY)
                .build();

            ResponseEntity<String> response = restTemplate.exchange(request, String.class);

            return extractDriveInfo(response.getBody());

        } catch (URISyntaxException e) {
            throw new CustomException(ErrorCode.URI_BAD_REQUEST);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REST_API_FAIL, e.getMessage());
        }
    }

    private DriveInfo extractDriveInfo(String body) {
        try {

            JsonNode rootNode = objectMapper.readTree(body);

            // "traoptimal" 배열 확인
            JsonNode traoptimalNode = rootNode.path("route").path("traoptimal");
            if (!traoptimalNode.isArray() || traoptimalNode.isEmpty()) {
                throw new CustomException(500, "REST_API_FAIL", "traoptimal 배열이 비어 있거나 존재하지 않습니다.");
            }

            // traoptimal 배열의 첫 번째 객체에서 distance와 duration 추출
            JsonNode firstTraoptimal = traoptimalNode.get(0);
            if (firstTraoptimal == null || !firstTraoptimal.has("summary")) {
                throw new CustomException(500, "REST_API_FAIL", "traoptimal의 첫 번째 객체에서 summary를 찾을 수 없습니다.");
            }

            log.info("[GeoService 좌표 조회] 조회 결과 : {}", firstTraoptimal);

            long duration = firstTraoptimal.path("summary").path("duration").asLong();
            long distance = firstTraoptimal.path("summary").path("distance").asLong();

            log.info("[GeoService 좌표 조회] Duration : {} milliseconds", duration);
            log.info("[GeoService 좌표 조회] Distance : {} meters", distance);

            return new DriveInfo(duration, distance);

        } catch(Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
