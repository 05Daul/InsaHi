package com.playdata.HumanResourceManagement.company.dao;

import com.playdata.HumanResourceManagement.company.entity.Company;
import com.playdata.HumanResourceManagement.company.repository.CompanyRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanyDAOImpl implements CompanyDAO {

  private final CompanyRepository companyRepository;

  //회사 정보 insert
  public void insert(Company company) {
    companyRepository.save(company);
  }

  // 회사 시간 추가 -김다울
  @Override
  public Company insertStartTime(String companyCode) {
    return companyRepository.save(companyCode);
  }

  // 김다울 -추가 회사 찾기
  @Override
  public Optional<Company> findByCompanyCode(String companyCode) {
    return companyRepository.findByCompanyCode(companyCode);
  }


}
