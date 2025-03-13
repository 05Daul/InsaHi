package com.playdata.HumanResourceManagement.employee.repository;

import com.playdata.HumanResourceManagement.company.entity.Company;
import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmployeeId(String employeeId);

    List<Employee> findByCompany_CompanyCode(String companyCode); // 회사 코드로 조회

    Employee findByEmployeeIdAndPasswordAndCompany(String employeeId, String password, Company company);

    boolean existsByDepartment(DepartmentEntity departmentEntity);

    // 수정된 메소드
    List<Employee> findByDepartment_DepartmentId(String departmentId);  // 부서 ID로 Employee 조회
    /// 김다울 - 회사 시간 가져오기
    @Query("SELECT e.company.startTime FROM Employee e WHERE e.employeeId = :employeeId")
    LocalTime findCompanyStartTimeByEmployeeId(@Param("employeeId") String employeeId);
    boolean existsByDepartment(DepartmentEntity departmentEntity);

    // 수정된 메소드
    List<Employee> findByDepartment_DepartmentId(String departmentId);  // 부서 ID로 Employee 조회
}
