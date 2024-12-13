package com.sparta.blackyolk.logistic_service.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;
    private Long createdBy;

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;
    private Long updatedBy;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;
    private Long deletedBy;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDeleted = false;

    protected BaseEntity(Long createdBy, Long updatedBy) {
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    protected void createFrom(Long createdUserId) {
        this.createdBy = createdUserId;
    }

    protected void updateFrom(Long updatedUserId) {
        this.updatedBy = updatedUserId;
    }

    protected void deleteFrom(Long deletedUserId) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedUserId;
    }
}
