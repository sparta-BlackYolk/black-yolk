package com.sparta.blackyolk.auth_service.courier.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_courier")
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID courierId;

    @Version
    private Long version; // 낙관적 잠금을 위한 버전 필드 추가

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String deliveryNum;

    @Column(nullable = true)
    private UUID hubId;

    @Column(nullable = false)
    private String slackId;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = true)
    private String createdBy;

    @Column(nullable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true)
    private String updatedBy;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @Column(nullable = true)
    private String deletedBy;
}
