package com.playdata.HumanResourceManagement.employee.service;

import com.playdata.HumanResourceManagement.employee.dto.AdminRequestDTO;
import com.playdata.HumanResourceManagement.employee.dto.LoginDTO;
import com.playdata.HumanResourceManagement.employee.dto.SmallProfileDTO;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import org.springframework.security.core.Authentication;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeResponseDTO;
import java.util.List;
import java.time.LocalTime;

public interface EmployeeService {

    Employee adminInsert(AdminRequestDTO adminRequestDTO);

    void addAdminAndUserRoles(Employee employee);

    Authentication login(LoginDTO employee);


    // Employee getUser(EmployeeRequestDTO employeeRequestDTO);


  EmployeeResponseDTO findEmployeeById(String employeeId);

  /// 김다울 추가
  LocalTime findCompanyStartTimeByEmployeeId(String employeeId);

  List<String> getAllEmployeeIds();

  // Employee getUser(EmployeeRequestDTO employeeRequestDTO);

    SmallProfileDTO getSmallProfile(String employeeId);
}