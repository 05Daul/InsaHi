package com.playdata.HumanResourceManagement.employee.service;

import com.playdata.HumanResourceManagement.employee.dto.*;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import org.springframework.security.core.Authentication;

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

    //mypage 왼쪽 작은 프로필
    ProfileCardDTO getProfileCard(String employeeId);

    EmployeeResponseDTO getEmployeeInfo(String employeeId);

    EmployeeResponseDTO updateEmployeeInfo(String employeeId);

    void addUserRoles(Employee employee);

    Employee employeeInsert(EmployeeRequestDTO employeeRequestDTO);
}