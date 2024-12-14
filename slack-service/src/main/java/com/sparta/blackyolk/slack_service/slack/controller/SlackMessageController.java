package com.sparta.blackyolk.slack_service.slack.controller;


import com.sparta.blackyolk.slack_service.slack.service.SlackMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/slack")
public class SlackMessageController {

    private final SlackMessageService slackMessageService;

    // 특정 사용자에게 메시지를 보내고 데이터베이스에 저장
    @PostMapping("/send")
    public String sendMessageToUser(@RequestParam String userId, @RequestParam String message) {
        boolean success = slackMessageService.sendMessage(userId, message);
        if (success) {
            return "메시지가 성공적으로 전송되었습니다.";
        } else {
            return "메시지 전송에 실패했습니다.";
        }
    }
}