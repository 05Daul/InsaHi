package com.playdata.HumanResourceManagement.department.repository;

import com.playdata.HumanResourceManagement.department.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface DepartmentRepository extends JpaRepository<DepartmentEntity, String> {

    // 1. 회사 코드로 모든 부서 조회
    List<DepartmentEntity> findByCompanyCode(String companyCode);

    // 2. 회사 코드와 부서 ID로 부서 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM DepartmentEntity d WHERE d.companyCode = :companyCode AND d.departmentId = :departmentId")
    void deleteByCompanyCodeAndDepartmentId(@Param("companyCode") String companyCode, @Param("departmentId") String departmentId);
}
