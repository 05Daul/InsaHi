package com.playdata.HumanResourceManagement.repository;

import com.playdata.HumanResourceManagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;  // List 임포트

@Repository
public interface HumanRepository extends JpaRepository<Employee, Integer> {

    // 모든 직원 조회 (회사 코드 기준)
    List<Employee> getEmployeesByCompany_CompanyCode(String companyCode);

    // 특정 직원 조회 (회사 코드 + 직원 ID)
    Employee getEmployeeByCompany_CompanyCodeAndEmployeeId(String companyCode, String employeeId);

    // 특정 직원 삭제 (회사 코드 + 직원 ID)
    void removeEmployeeByCompany_CompanyCodeAndEmployeeId(String companyCode, String employeeId);

}
