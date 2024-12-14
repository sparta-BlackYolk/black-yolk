package com.sparta.msa_exam.order.order_service.domain.client;

import com.sparta.msa_exam.order.order_service.application.dto.response.CompanyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "company-service")
public interface CompanyClient {

    @GetMapping("/companies/search-by-name")
    List<CompanyResponseDto> findCompaniesByName(@RequestParam("name") String name);

    @GetMapping("/companies/{companyId}")
    CompanyResponseDto findCompanyById(@PathVariable("companyId") UUID companyId);

    @PostMapping("/companies/batch")
    List<CompanyResponseDto> findCompaniesByIds(@RequestBody List<UUID> ids);
}
