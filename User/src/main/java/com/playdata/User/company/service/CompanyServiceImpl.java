package com.playdata.User.company.service;

import com.playdata.User.company.dao.CompanyDAO;
import com.playdata.User.company.dto.CompanyRequestDTO;
import com.playdata.User.company.entity.Company;
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
        Company entity = modelMapper.map(companyRequestDTO, Company.class);

        companyDAO.insert(entity);
        return entity;
    }

}
