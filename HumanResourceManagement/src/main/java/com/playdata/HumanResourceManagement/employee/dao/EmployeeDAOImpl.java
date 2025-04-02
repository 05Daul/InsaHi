package com.playdata.HumanResourceManagement.employee.dao;

import com.playdata.HumanResourceManagement.employee.entity.Authority;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import com.playdata.HumanResourceManagement.employee.repository.AuthorityRepository;
import com.playdata.HumanResourceManagement.employee.repository.EmployeeRepository;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
@RequiredArgsConstructor
public class EmployeeDAOImpl implements EmployeeDAO {

  private final EmployeeRepository employeeRepository;
  private final AuthorityRepository authorityRepository;

  @Override
  public List<Employee> findByAuthorityList_AuthorityName(String authorityName) {
    return employeeRepository.findByAuthorityList_AuthorityName(authorityName);
  }

  @Override
  public void addAdminRoleToEmployee(String employeeId) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new RuntimeException("직원 정보를 찾을 수 없습니다."));

    Authority adminAuthority = authorityRepository.findByAuthorityName("ROLE_ADMIN")
        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN 권한이 존재하지 않습니다."));

    if (!employee.getAuthorityList().contains(adminAuthority)) {
      employee.getAuthorityList().add(adminAuthority);
      employeeRepository.save(employee);
    }
  }

  @Override
  public void removeAdminRoleFromEmployee(String employeeId) {
    Employee employee = employeeRepository.findById(employeeId)
        .orElseThrow(() -> new RuntimeException("직원 정보를 찾을 수 없습니다."));
    Authority adminAuthority = authorityRepository.findByAuthorityName("ROLE_ADMIN")
        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN 권한이 존재하지 않습니다."));

    if (employee.getAuthorityList().contains(adminAuthority)) {
      employee.getAuthorityList().remove(adminAuthority);
      employeeRepository.save(employee);
    }
  }

  @Override
  public void insert(Employee employee) {
    employeeRepository.save(employee);
  }

  @Override
  public List<Authority> findAuthoritiesByCompanyCode(String companyCode) {
    return employeeRepository.findAuthoritiesByCompanyCode(companyCode);
  }

  @Override
  public Employee findById(String employeeId) {
    System.out.println("dao단 = " + employeeId);
    return employeeRepository.findById(employeeId).orElse(null);
  }

  @Override
  public Employee findByEmployeeId(String employeeId) {
    return employeeRepository.findById(employeeId).orElse(null);
  }

  @Override
  public LocalTime findCompanyStartTimeByEmployeeId(String employeeId) {
    LocalTime companyStartTime = employeeRepository.findCompanyStartTimeByEmployeeId(employeeId);
    Employee employee = employeeRepository.findById(employeeId).orElse(null);
    log.info("Employee Entity: {}", employee);
    log.info("Company Entity: {}", employee.getCompany());
    log.info("Company StartTime: {}", employee.getCompany().getStartTime());

    System.out.println("dao단 = " + employeeId);
    log.info("dao단 getCompanyStartTime: {}", companyStartTime);
    return companyStartTime;
  }

  @Override
  public void update(Employee employee) {
    employeeRepository.save(employee);
  }

  @Override
  public List<Employee> findAll() {
    return employeeRepository.findAll();
  }


}
