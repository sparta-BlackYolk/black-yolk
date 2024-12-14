package com.sparta.blackyolk.slack_service.slack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class SlackMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;  // 메시지 고유 ID

    private String userId;  // 수신자 Slack 사용자 ID

    private String message;  // 보낸 메시지

    private LocalDateTime sentTime;  // 메시지 발송 시간

    // 메시지 상태를 나타낼 수 있는 필드 (예: 전송 성공/실패 등)
    private boolean success;

    public static SlackMessage create(String userId, String message, LocalDateTime sentTime, Boolean success) {
        return SlackMessage.builder()
                .userId(userId)
                .message(message)
                .sentTime(sentTime)
                .success(success)
                .build();
    }
}
