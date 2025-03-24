package com.playdata.HumanResourceManagement.employee.dao;

import com.playdata.HumanResourceManagement.employee.entity.Employee;
import com.playdata.HumanResourceManagement.employee.repository.EmployeeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import java.time.LocalTime;


@Slf4j
@Repository
@RequiredArgsConstructor
public class EmployeeDAOImpl implements EmployeeDAO {

  private final EmployeeRepository employeeRepository;

  @Override
  public void insert(Employee employee) {
    employeeRepository.save(employee);
  }





  @Override
  public Employee findById(String employeeId) {
    System.out.println("dao단 = " + employeeId);
    return employeeRepository.findById(employeeId).orElse(null);
    }

    @Override
    public LocalTime findCompanyStartTimeByEmployeeId(String employeeId) {
        LocalTime companyStartTime = employeeRepository.findCompanyStartTimeByEmployeeId(employeeId);
        employeeRepository.findById(employeeId).orElse(null);
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
