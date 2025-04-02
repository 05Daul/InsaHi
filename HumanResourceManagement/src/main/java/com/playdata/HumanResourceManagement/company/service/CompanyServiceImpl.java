package com.playdata.HumanResourceManagement.company.service;

import com.playdata.HumanResourceManagement.company.dao.CompanyDAO;
import com.playdata.HumanResourceManagement.company.dto.CompanyRequestDTO;
import com.playdata.HumanResourceManagement.company.dto.CompanyResponseDTO;
import com.playdata.HumanResourceManagement.company.entity.Company;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

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


    // 회사 출근 시간 1) insert
    @Override
    public CompanyResponseDTO insertStartTime(CompanyResponseDTO companyResponseDTO) {

        Company entity = findByCompanyCode(companyResponseDTO.getCompanyCode()).orElseThrow(()
        -> new RuntimeException("회사가 존재치 않습니다."));
        entity.setStartTime(companyResponseDTO.getStartTime());
        companyDAO.insert(entity);
        return modelMapper.map(entity, CompanyResponseDTO.class);
    }
    // 회사 출근 시간  컨트롤러 에서 2) insert 사전
    @Override
    public Optional<Company> findByCompanyCode(String companyCode) {
            return companyDAO.findByCompanyCode(companyCode);
    }
}
