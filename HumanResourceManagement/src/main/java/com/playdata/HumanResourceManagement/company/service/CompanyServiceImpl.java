package com.playdata.HumanResourceManagement.company.service;

import com.playdata.HumanResourceManagement.company.dao.CompanyDAO;
import com.playdata.HumanResourceManagement.company.dto.CompanyRequestDTO;
import com.playdata.HumanResourceManagement.company.entity.Company;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyDAO companyDAO;
    private final ModelMapper modelMapper;

    //회사 정보 insert
    @Override
    public Company insert(CompanyRequestDTO companyRequestDTO) {
        System.out.println(companyRequestDTO);
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Company entity = modelMapper.map(companyRequestDTO, Company.class);

        companyDAO.insert(entity);
        return entity;
    }

}
