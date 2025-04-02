package com.playdata.HumanResourceManagement.employee.dao;

import com.playdata.HumanResourceManagement.employee.entity.Authority;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import java.time.LocalTime;
import java.util.List;

public interface EmployeeDAO {


  // 회원 정보 수정
  void insert(Employee employee);

  Employee findById(String employeeId);

  Employee findByEmployeeId(String employeeId);

  List<Employee> findAll();

  ///  김다울 추가
  LocalTime findCompanyStartTimeByEmployeeId(String employeeId);

  List<Employee> findByAuthorityList_AuthorityName(String authorityName);

  void addAdminRoleToEmployee(String employeeId);

  List<Authority> findAuthoritiesByCompanyCode(String companyCode);

  void update(Employee employee);

  void removeAdminRoleFromEmployee(String employeeId);
}
