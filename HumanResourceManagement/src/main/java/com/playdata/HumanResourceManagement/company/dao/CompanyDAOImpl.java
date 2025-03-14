package com.playdata.HumanResourceManagement.company.dao;

import com.playdata.HumanResourceManagement.company.entity.Company;
import com.playdata.HumanResourceManagement.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanyDAOImpl extends CompanyDAO {
    private final CompanyRepository companyRepository;

    //회사 정보 insert
    public void insert(Company company) {
        companyRepository.save(company);
    }
}
