package com.sparta.blackyolk.slack_service.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.blackyolk.slack_service.domain.entity.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

    private final RestTemplate restTemplate;

    @Value("${gemini.token}")
    private String geminiToken;
    // 모델 종류
    private static final String MODEL_NAME = "gemini-1.5-flash-latest";

    public GeminiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String getFinalShippingDeadline(Order order) {
        // Gemini API 요청 URL
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + MODEL_NAME + ":generateContent?key=" + geminiToken;

        // 주문 데이터를 포함한 JSON 요청 본문 생성
        String requestBody = buildRequestBody(order);

        // HTTP 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // HTTP 요청 엔티티 설정
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Gemini API 호출
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

        // 응답 처리 (최종 발송 시한 추출)
        if (response.getStatusCode() == HttpStatus.OK) {
            return extractFinalDeadline(response.getBody());
        } else {
            throw new RuntimeException("Gemini API 호출 실패: " + response.getStatusCode());
        }
    }

    // Gemini API에 보낼 JSON 요청 본문 생성
    private String buildRequestBody(Order order) {
        String orderDescription = String.format(
                "주문 번호: %s\n" +
                        "주문자: %s / %s\n" +
                        "상품 정보: %s\n" +
                        "요청 사항: %s\n" +
                        "발송지: %s\n" +
                        "경유지: %s\n" +
                        "도착지: %s\n" +
                        "배송 담당자: %s / %s" +
                        "요청 정보를 모두 고려하여 이 때까진 보내야 납기에 맞출 수 있다 하는 마지막 시점. 즉, 최종 발송 시한을 추정해줘." +
                        "발송지부터 경유지, 도착지까지 거리들이랑 시간 잘 고려해야해." +
                        "나한테는 답으로 시한은 날짜랑 시간까지 포함해서" +
                        "위 내용을 기반으로 도출된 최종 발송 시한은 ~ 입니다. 라고만 답해줘",
                order.getOrderId(),
                order.getCustomerName(), order.getCustomerEmail(),
                order.getProductInfo(),
                order.getDeliveryRequest(),
                order.getOrigin(),
                String.join(", ", order.getTransitCenters()),
                order.getDestination(),
                order.getDeliveryContact(), order.getDeliveryContact());

        // Gemini API 요청 본문 형식으로 텍스트를 변환
        return String.format("{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}]}", orderDescription);
    }

    private String extractFinalDeadline(String responseBody) {
        try {
            // Jackson의 ObjectMapper를 사용해 JSON을 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // candidates 배열의 첫 번째 항목을 가져옴
            JsonNode candidatesNode = rootNode.path("candidates");
            if (candidatesNode.isArray() && !candidatesNode.isEmpty()) {
                // 첫 번째 항목에서 content.parts[0].text를 추출
                JsonNode textNode = candidatesNode.get(0).path("content").path("parts").get(0).path("text");

                // 텍스트가 존재하면 반환, 없으면 기본 메시지 반환
                if (!textNode.isMissingNode()) {
                    return textNode.asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "발송 시한을 계산할 수 없습니다.";
    }
}
