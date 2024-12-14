package com.sparta.blackyolk.slack_service.slack.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.blackyolk.slack_service.slack.entity.SlackMessage;
import com.sparta.blackyolk.slack_service.slack.repository.SlackMessageRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
public class SlackMessageService {

    private final RestTemplate restTemplate;
    private final SlackMessageRepository slackMessageRepository;

    @Value("${slack.bot.token}")
    private String slackBotToken;

    public SlackMessageService(RestTemplate restTemplate, SlackMessageRepository slackMessageRepository) {
        this.restTemplate = restTemplate;
        this.slackMessageRepository = slackMessageRepository;
    }

    public boolean sendMessage(String userId, String message) {
        // DM 채널 열기
        String openUrl = "https://slack.com/api/conversations.open";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + slackBotToken);
        headers.set("Content-Type", "application/json");

        // DM 채널 열기 위한 요청 본문
        Map<String, String> openBody = new HashMap<>();
        openBody.put("users", userId);  // DM을 보낼 사용자 ID

        // 요청을 RestTemplate으로 전송하여 DM 채널 열기
        HttpEntity<Map<String, String>> openEntity = new HttpEntity<>(openBody, headers);
        ResponseEntity<String> openResponse = restTemplate.exchange(openUrl, HttpMethod.POST, openEntity, String.class);

        log.info("Slack API Response (Open): " + openResponse.getBody());

        // 채널 ID 추출
        String channelId = extractChannelIdFromResponse(openResponse.getBody());

        if (channelId == null) {
            return false;  // 채널 ID가 없으면 실패
        }

        // 메시지 보내기
        String sendUrl = "https://slack.com/api/chat.postMessage";
        Map<String, String> body = new HashMap<>();
        body.put("channel", channelId);  // 열었던 DM 채널 ID
        body.put("text", message);       // 보낼 메시지

        HttpEntity<Map<String, String>> sendEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> sendResponse = restTemplate.exchange(sendUrl, HttpMethod.POST, sendEntity, String.class);

        log.info("Slack API Response (Send): " + sendResponse.getBody());

        // 메시지 저장
        slackMessageRepository.save(SlackMessage.create(userId, message, LocalDateTime.now(),
                sendResponse.getStatusCode().is2xxSuccessful()));

        return sendResponse.getStatusCode().is2xxSuccessful();
    }

    private String extractChannelIdFromResponse(String response) {
        try {
            // ObjectMapper를 사용하여 응답 JSON을 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);

            // "channel" 필드를 가져오고, 그 안의 "id" 값을 추출
            JsonNode channelNode = rootNode.path("channel");
            if (channelNode.isObject()) {
                return channelNode.path("id").asText();  // 채널 ID를 반환
            }

        } catch (Exception e) {
            log.error("Error extracting channel ID from response", e);
        }
        return null;  // 오류가 발생하면 null 반환
    }
}
