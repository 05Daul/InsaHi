package com.playdata.HumanResourceManagement.repository;

import com.playdata.HumanResourceManagement.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {

    // 1. 회사 코드로 모든 부서 조회
    List<Department> findByCompanyCode(String companyCode);

    // 2. 회사 코드로 부서 계층으로 조회
    List<Department> findByCompanyCodeAndDepartmentLevel(String companyCode, int departmentLevel);

    // 3. 회사 코드로 특정 부서 ID 조회
    Optional<Department> findByDepartmentIdAndCompanyCode(String departmentId, String companyCode);

    // 4. 부서 저장
    List<Department> saveAll(List<Department> departments);

    // 5. 회사 코드로 모든 부서 삭제
    void deleteByCompanyCode(String companyCode);
}
