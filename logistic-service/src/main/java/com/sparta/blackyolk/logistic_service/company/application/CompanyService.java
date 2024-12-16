package com.sparta.blackyolk.logistic_service.company.application;

import com.sparta.blackyolk.logistic_service.common.exception.CustomException;
import com.sparta.blackyolk.logistic_service.common.exception.ErrorCode;
import com.sparta.blackyolk.logistic_service.common.service.UserService;
import com.sparta.blackyolk.logistic_service.company.application.dto.CompanyRequestDto;
import com.sparta.blackyolk.logistic_service.company.application.dto.CompanyResponseDto;
import com.sparta.blackyolk.logistic_service.company.application.dto.UserData;
import com.sparta.blackyolk.logistic_service.company.domain.Company;
import com.sparta.blackyolk.logistic_service.company.domain.CompanyRepository;
import com.sparta.blackyolk.logistic_service.hub.application.domain.Hub;
import com.sparta.blackyolk.logistic_service.hub.application.service.HubCacheService;
import com.sparta.blackyolk.logistic_service.hub.data.HubEntity;
import com.sparta.blackyolk.logistic_service.hub.framework.repository.HubReadOnlyRepository;
import com.sparta.blackyolk.logistic_service.hub.framework.repository.HubRepository;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final HubReadOnlyRepository hubReadOnlyRepository;
    private final UserService userService;

    public CompanyResponseDto createCompany(CompanyRequestDto requestDto) {
        // User 존재 여부 확인
        UserData user = userService.getUserById(requestDto.getUser_id())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        // Hub 존재 여부 확인
        HubEntity hub = hubReadOnlyRepository.findByHubIdAndIsDeletedFalse(String.valueOf(requestDto.getHub_id()))
                .orElseThrow(() -> new CustomException(ErrorCode.HUB_NOT_EXIST));

        Company company = companyRepository.save(Company.create(requestDto));

        return CompanyResponseDto.toResponseDto(company);
    }

    public CompanyResponseDto getCompany(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("업체 정보가 없습니다."));

        return CompanyResponseDto.toResponseDto(company);
    }

    @Transactional
    public CompanyResponseDto updateCompany(UUID companyId, CompanyRequestDto requestDto) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("업체 정보가 없습니다."));

        company.update(requestDto);
        return CompanyResponseDto.toResponseDto(company);
    }

    @Transactional
    public void deleteCompany(UUID companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("업체 정보가 없습니다."));

        company.delete();
    }

}
