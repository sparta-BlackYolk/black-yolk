package com.sparta.blackyolk.logistic_service.company.application.dto;

import com.sparta.blackyolk.logistic_service.company.domain.Company;
import com.sparta.blackyolk.logistic_service.company.domain.CompanyType;
import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class CompanyResponseDto {

    private UUID id;

    private String name;

    private CompanyType companyType;

    private String hub_id;

    private Long user_id;

    public static CompanyResponseDto toResponseDto(Company company) {
        return CompanyResponseDto.builder()
                .id(company.getId())
                .name(company.getName())
                .companyType(company.getCompanyType())
                .hub_id(company.getHub_id())
                .user_id(company.getUser_id())
                .build();
    }
}
