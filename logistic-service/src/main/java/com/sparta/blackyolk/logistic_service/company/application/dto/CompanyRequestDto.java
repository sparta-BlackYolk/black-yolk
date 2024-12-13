package com.sparta.blackyolk.logistic_service.company.application.dto;

import com.sparta.blackyolk.logistic_service.company.entity.CompanyType;
import jakarta.persistence.*;
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

    private Long hub_id;

    private Long user_id;

}
