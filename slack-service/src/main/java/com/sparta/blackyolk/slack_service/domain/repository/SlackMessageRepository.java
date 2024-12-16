package com.sparta.blackyolk.slack_service.domain.repository;

import com.sparta.blackyolk.slack_service.domain.entity.SlackMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SlackMessageRepository extends JpaRepository<SlackMessage, UUID> {
}
