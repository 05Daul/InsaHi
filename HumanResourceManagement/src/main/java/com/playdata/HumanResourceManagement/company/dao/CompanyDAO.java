package com.playdata.HumanResourceManagement.company.dao;

import com.playdata.HumanResourceManagement.company.entity.Company;
import java.util.Optional;


public interface CompanyDAO {

  public void insert(Company entity);

  Company insertStartTime(String companyCode);

  Optional<Company> findByCompanyCode(String companyCode);
}
