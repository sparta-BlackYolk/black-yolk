package com.sparta.blackyolk.slack_service.slack.repository;

import com.sparta.blackyolk.slack_service.slack.entity.SlackMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SlackMessageRepository extends JpaRepository<SlackMessage, UUID> {
}
