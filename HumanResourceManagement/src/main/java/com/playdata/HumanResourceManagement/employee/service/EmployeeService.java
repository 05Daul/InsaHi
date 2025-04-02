package com.playdata.HumanResourceManagement.employee.service;

import com.playdata.HumanResourceManagement.employee.dto.AdminRequestDTO;
import com.playdata.HumanResourceManagement.employee.dto.AuthorityResponseDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmpAuthResponseDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeRequestDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeResponseDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeUpdateDTO;
import com.playdata.HumanResourceManagement.employee.dto.LoginDTO;
import com.playdata.HumanResourceManagement.employee.dto.ProfileCardDTO;
import com.playdata.HumanResourceManagement.employee.dto.UpdatePasswordDTO;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import java.time.LocalTime;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {

  void updateEmployee(String employeeId, Long positionSalaryId);

  Employee adminInsert(AdminRequestDTO adminRequestDTO);

  // Employee adminDelete(AdminRequestDTO adminRequestDTO);
  void addAdminAndUserRoles(Employee employee);

  Authentication login(LoginDTO employee);

  // Employee getUser(EmployeeRequestDTO employeeRequestDTO);


  EmployeeResponseDTO findEmployeeById(String employeeId);

  /// 김다울 추가
  LocalTime findCompanyStartTimeByEmployeeId(String employeeId);

  List<String> getAllEmployeeIds();

  // Employee getUser(EmployeeRequestDTO employeeRequestDTO);
  List<AuthorityResponseDTO> findAuthoritiesByCompanyCode(String companyCode);

  List<EmpAuthResponseDTO> findByAuthorityList_AuthorityName(String authorityName);

  //mypage 왼쪽 작은 프로필
  ProfileCardDTO getProfileCard(String employeeId);

  EmployeeResponseDTO getEmployeeInfo(String employeeId);

  EmployeeResponseDTO updateEmployeeInfo(String employeeId,
      EmployeeUpdateDTO updateDTO, MultipartFile profileImage);

  void addUserRoles(Employee employee);

  Employee employeeInsert(EmployeeRequestDTO employeeRequestDTO);

  EmployeeResponseDTO updatePassword(String employeeId, UpdatePasswordDTO updatePasswordDTO);

  List<Employee> getMemberList();

  void addAdminRoleToEmployee(String employeeId);

  void removeAdminRoleFromEmployee(String employeeId);
}