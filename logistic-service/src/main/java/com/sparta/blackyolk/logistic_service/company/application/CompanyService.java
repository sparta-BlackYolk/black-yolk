package com.sparta.blackyolk.logistic_service.company.application;

import com.sparta.blackyolk.logistic_service.company.application.dto.CompanyRequestDto;
import com.sparta.blackyolk.logistic_service.company.application.dto.CompanyResponseDto;
import com.sparta.blackyolk.logistic_service.company.entity.Company;
import com.sparta.blackyolk.logistic_service.company.repository.CompanyRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyResponseDto createCompany(CompanyRequestDto requestDto) {
        Company company = companyRepository.save(Company.create(requestDto));

        return CompanyResponseDto.toResponseDto(company);
    }

    public CompanyResponseDto getCompany(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("업체 정보가 없습니다."));

        return CompanyResponseDto.toResponseDto(company);
    }

}
