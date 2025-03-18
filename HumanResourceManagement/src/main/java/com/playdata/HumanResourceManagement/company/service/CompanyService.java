package com.playdata.HumanResourceManagement.company.service;


import com.playdata.HumanResourceManagement.company.dto.CompanyRequestDTO;
import com.playdata.HumanResourceManagement.company.entity.Company;

public interface CompanyService {
    Company insert(CompanyRequestDTO companyRequestDTO);
}
