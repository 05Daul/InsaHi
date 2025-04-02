package com.playdata.HumanResourceManagement.company.repository;

import com.playdata.HumanResourceManagement.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByCompanyCode(String companyCode);
    Company save(String companyCode); // 김다울 추가

}
