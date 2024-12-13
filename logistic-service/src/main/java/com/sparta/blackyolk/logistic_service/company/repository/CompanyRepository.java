package com.sparta.blackyolk.logistic_service.company.repository;

import com.sparta.blackyolk.logistic_service.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

}
