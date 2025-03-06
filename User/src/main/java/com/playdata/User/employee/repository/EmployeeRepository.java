package com.playdata.User.employee.repository;

import com.playdata.User.company.entity.Company;
import com.playdata.User.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Employee findByEmployeeId(String employeeId);

    Employee findByEmployeeIdAndPasswordAndCompany(String employeeId, String Password, Company company);


}