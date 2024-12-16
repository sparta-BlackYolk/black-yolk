package com.sparta.blackyolk.logistic_service.company.domain;

import com.sparta.blackyolk.logistic_service.company.application.dto.CompanyRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@Table(name = "p_company")
public class Company extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private CompanyType companyType;

    @Column(nullable = false)
    private String hub_id;
    // 허브 정보: Hub 테이블과 연관관계 설정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "hub_id", referencedColumnName = "id")
//    private Hub hub;

    @Column(nullable = false)
    private Long user_id;
    // 담당자 정보: User 테이블과 연관관계 설정
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user; // 업체 담당자

    @Column(nullable = false)
    private Boolean is_deleted;

    @PreUpdate
    public void updateDeleteField() {
        if (this.is_deleted && super.getDeletedAt() == null) {
            super.markDeleted();
        }
    }

    public static Company create(CompanyRequestDto requestDto) {
        return Company.builder()
                .name(requestDto.getName())
                .companyType(requestDto.getCompanyType())
                .hub_id(requestDto.getHub_id())
                .user_id(requestDto.getUser_id())
                .is_deleted(false)
                .build();
    }

    public void update(CompanyRequestDto requestDto) {
        this.name = requestDto.getName();
        this.companyType = requestDto.getCompanyType();
        this.hub_id = requestDto.getHub_id();
        this.user_id = requestDto.getUser_id();
    }

    public void delete() {
        this.is_deleted = true;
    }

}
