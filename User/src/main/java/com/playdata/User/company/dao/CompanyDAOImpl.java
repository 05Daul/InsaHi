package com.playdata.User.company.dao;

import com.playdata.User.company.entity.Company;
import com.playdata.User.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanyDAOImpl implements CompanyDAO{
    private final CompanyRepository companyRepository;

    //회사 정보 insert
    public void insert(Company company) {
        companyRepository.save(company);
    }
}
