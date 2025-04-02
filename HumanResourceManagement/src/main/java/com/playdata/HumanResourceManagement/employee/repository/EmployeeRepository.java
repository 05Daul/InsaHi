package com.playdata.HumanResourceManagement.employee.repository;

import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
import com.playdata.HumanResourceManagement.employee.entity.Authority;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

  @EntityGraph(attributePaths = "authorityList")
  Employee findByEmployeeId(String employeeId);

  List<Employee> findByCompany_CompanyCode(String companyCode); // 회사 코드로 조회

  List<Employee> findAll();

  // 부서 ID로 직원 조회

  // 회사 출근 시간 조회
  @Query("SELECT e FROM Employee e WHERE e.department = :departmentEntity")
  boolean existsByDepartment(@Param("departmentEntity") DepartmentEntity departmentEntity);


  // 수정된 메소드
  List<Employee> findByDepartment_DepartmentId(String departmentId);  // 부서 ID로 Employee 조회

  @Query("SELECT e.company.startTime FROM Employee e WHERE e.employeeId = :employeeId")
  LocalTime findCompanyStartTimeByEmployeeId(@Param("employeeId") String employeeId);

  List<Employee> findByAuthorityList_AuthorityName(String authorityName);

  // 회사의 권한 조회
  @Query("SELECT DISTINCT a FROM Employee e JOIN e.authorityList a WHERE e.company.companyCode = :companyCode")
  List<Authority> findAuthoritiesByCompanyCode(@Param("companyCode") String companyCode);

}
