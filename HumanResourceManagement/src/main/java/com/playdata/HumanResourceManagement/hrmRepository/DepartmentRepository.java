package com.playdata.HumanResourceManagement.hrmRepository;

import com.playdata.HumanResourceManagement.hrmEntity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


public interface DepartmentRepository extends JpaRepository<DepartmentEntity, String> {
//
//    // 1. 회사 코드로 모든 부서 조회
//    List<DepartmentEntity> findByCompanyCode(String companyCode);
//
//    // 2. 회사 코드로 부서 계층으로 조회
//    List<DepartmentEntity> findByCompanyCodeAndDepartmentLevel(String companyCode, int departmentLevel);
//
//    // 3. 회사 코드로 특정 부서 ID 조회
//    Optional<DepartmentEntity> findByDepartmentIdAndCompanyCode(String departmentId, String companyCode);
//
//    // 4. 회사 코드로 모든 부서 삭제
//    void deleteByCompanyCode(String companyCode);
//
}
