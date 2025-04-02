package com.playdata.HumanResourceManagement.company.service;


import com.playdata.HumanResourceManagement.company.dto.CompanyRequestDTO;
import com.playdata.HumanResourceManagement.company.dto.CompanyResponseDTO;
import com.playdata.HumanResourceManagement.company.entity.Company;

import java.util.Optional;

public interface CompanyService {
    Company insert(CompanyRequestDTO companyRequestDTO);
    CompanyResponseDTO insertStartTime(CompanyResponseDTO companyRequestDTO);
    Optional<Company> findByCompanyCode(String companyCode);


}
