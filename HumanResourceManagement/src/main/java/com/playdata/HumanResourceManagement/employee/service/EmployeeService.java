package com.playdata.HumanResourceManagement.employee.service;

import com.playdata.HumanResourceManagement.employee.dto.AdminRequestDTO;
import com.playdata.HumanResourceManagement.employee.dto.LoginDTO;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import org.springframework.security.core.Authentication;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeRequestDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeResponseDTO;
import java.util.List;
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface EmployeeService {

    Employee adminInsert(AdminRequestDTO adminRequestDTO);

    void addAdminAndUserRoles(Employee employee);

    Authentication login(LoginDTO employee);


    // Employee getUser(EmployeeRequestDTO employeeRequestDTO);

  Employee insertEmployee(EmployeeRequestDTO employeeRequestDTO);  // 기존 insert문 (이거 사용한분 수정할 필요 있음.)

  EmployeeResponseDTO findEmployeeById(String employeeId);

  /// 김다울 추가
  LocalTime findCompanyStartTimeByEmployeeId(String employeeId);

  List<String> getAllEmployeeIds();

  // Employee getUser(EmployeeRequestDTO employeeRequestDTO);
}
