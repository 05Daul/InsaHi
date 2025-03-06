package com.playdata.User.employee.service;

import com.playdata.User.employee.dto.EmployeeRequestDTO;
import com.playdata.User.employee.dto.LoginDTO;
import com.playdata.User.employee.entity.Employee;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.Authentication;

public interface EmployeeService {

    Employee insertEmployee(EmployeeRequestDTO employeeRequestDTO);

    void addAdminAndUserRoles(Employee employee);

    Authentication signin(LoginDTO employee);
}
