package com.sparta.blackyolk.logistic_service.company.application.dto;

import com.sparta.blackyolk.logistic_service.company.domain.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequestDto {

    private String name;

    private CompanyType companyType;

    private String hub_id;

    private Long user_id;

    private String authorization;

}
