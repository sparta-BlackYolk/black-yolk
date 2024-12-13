package com.sparta.blackyolk.auth_service.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "p_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 255)
    private String slackId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRoleEnum role = UserRoleEnum.VENDOR_MANAGER;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    private String createdBy;
    private String updatedBy;
    private String deletedBy;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    // @PrePersist를 사용해 엔티티가 저장되기 전에 created_at 설정
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
    }


    public User(String username, String email, String password, String slackId, UserRoleEnum role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.slackId = slackId;
        this.role = role;
    }
}
