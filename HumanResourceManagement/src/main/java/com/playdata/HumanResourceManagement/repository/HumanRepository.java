package com.playdata.HumanResourceManagement.repository;

import com.playdata.HumanResourceManagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HumanRepository extends JpaRepository<Employee, Integer> {

    // 1. 회사 코드로 모든 직원 검색
    List<Employee> findByCompanyCode(String companyCode);

    // 2. 회사 코드로 특정 직원 검색
    Employee findByCompanyCodeAndEmployeeId(String companyCode, Integer employeeId);

    // 3. 회사 코드로 특정 직원 삭제 (직원 ID 기준)
    void deleteByCompanyCodeAndEmployeeId(String companyCode, Integer employeeId);

    // 4. 회사 코드로 특정 직원 추가
    Employee save(Employee employee);

    // 5. 회사코드로 특정 직원 권한 변경


}

