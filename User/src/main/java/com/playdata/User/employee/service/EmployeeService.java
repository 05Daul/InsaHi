package com.playdata.User.employee.service;

import com.playdata.User.employee.dto.EmployeeRequestDTO;
import com.playdata.User.employee.dto.LoginDTO;
//import org.springframework.security.core.Authentication;

public interface EmployeeService {

    void insertEmployee(EmployeeRequestDTO employeeRequestDTO);

//    Authentication signin(LoginDTO employee);
}
