package com.sparta.blackyolk.logistic_service.company.controller;

import com.sparta.blackyolk.logistic_service.company.application.CompanyService;
import com.sparta.blackyolk.logistic_service.company.application.dto.CompanyRequestDto;
import com.sparta.blackyolk.logistic_service.company.application.dto.CompanyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/companies")
    public ResponseEntity<CompanyResponseDto> createCompany(@RequestBody CompanyRequestDto requestDto) {
        CompanyResponseDto responseDto = companyService.createCompany(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/companies/{company_id}")
    public ResponseEntity<CompanyResponseDto> getCompany(@PathVariable(name = "company_id") UUID company_id) {

        return ResponseEntity.ok(companyService.getCompany(company_id));
    }

    @PutMapping("/companies/{company_id}")
    public ResponseEntity<CompanyResponseDto> updateCompany(@PathVariable(name = "company_id") UUID company_id,
                                                            @RequestBody CompanyRequestDto requestDto) {
        CompanyResponseDto responseDto = companyService.updateCompany(company_id, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/companies/{company_id}")
    public ResponseEntity<String> deleteCompany(@PathVariable(name = "company_id") UUID company_id) {
        companyService.deleteCompany(company_id);

        return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
    }


}
