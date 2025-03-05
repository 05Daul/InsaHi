package com.playdata.User.company.dao;

import com.playdata.User.company.entity.Company;
import com.playdata.User.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


public interface CompanyDAO {
    public void insert(Company company);
}
