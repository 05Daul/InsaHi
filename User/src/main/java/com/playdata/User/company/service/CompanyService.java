package com.playdata.User.company.service;

import com.playdata.User.company.dto.CompanyRequestDTO;
import com.playdata.User.company.entity.Company;

public interface CompanyService {
    Company insert(CompanyRequestDTO companyRequestDTO);
}
