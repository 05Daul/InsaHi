package com.playdata.HumanResourceManagement.employee.service;

import com.playdata.HumanResourceManagement.employee.authentication.EmpAuthenticationToken;
import com.playdata.HumanResourceManagement.employee.dao.AuthorityDAO;
import com.playdata.HumanResourceManagement.employee.dao.EmployeeDAO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import com.playdata.HumanResourceManagement.employee.dto.AdminRequestDTO;
import com.playdata.HumanResourceManagement.employee.dto.LoginDTO;
import com.playdata.HumanResourceManagement.employee.entity.Authority;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import lombok.RequiredArgsConstructor;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeRequestDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeResponseDTO;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;
    private final AuthorityDAO authorityDAO;
    private final ModelMapper modelMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Employee adminInsert(AdminRequestDTO adminRequestDTO) {

        Employee entity = modelMapper.map(adminRequestDTO, Employee.class);

        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        employeeDAO.insert(entity);
        return entity;
    }

    // ROLE_ADMIN + ROLE_USER 권한 부여 (DAO 활용)
    @Override
    public void addAdminAndUserRoles(Employee employee) {
        Set<Authority> roles = new HashSet<>();

        // DAO에서 ROLE_ADMIN, ROLE_USER 가져오기
        authorityDAO.getAdminRole().ifPresent(roles::add);
        authorityDAO.getUserRole().ifPresent(roles::add);

        // Employee에 권한 추가
        employee.setAuthorityList(roles);
        employeeDAO.insert(employee);
    }

    @Override
    public Authentication login(LoginDTO employee) {
        //스프링시큐리티의 인증이 실행되도록 처리

        // 1. 커스텀 인증 토큰 (EmpAuthenticationToken) 생성
        EmpAuthenticationToken token =
                new EmpAuthenticationToken(
                        employee.getEmployeeId(),
                        employee.getPassword(),
                        employee.getCompanyCode()
                );

        // 2. Spring Security의 인증 시스템을 사용하여 인증 수행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);


        return authentication;

    }
  
   @Override
  public EmployeeResponseDTO findEmployeeById(String employeeId) {
    Employee employee = employeeDAO.findById(employeeId);
    System.out.println("employee 서비스단 = " + employee.getEmployeeId());
    System.out.println(
        "employee.getCompany().getCompanyCode() = " + employee.getCompany().getCompanyCode());
    return modelMapper.map(employee, EmployeeResponseDTO.class);
  }
  
  @Override
  /// 김다울 추가
  public LocalTime findCompanyStartTimeByEmployeeId(String employeeId) {
    return employeeDAO.findCompanyStartTimeByEmployeeId(employeeId);
  }
  
  @Override
  public Employee insertEmployee(EmployeeRequestDTO employeeRequestDTO) {

    Employee entity = modelMapper.map(employeeRequestDTO, Employee.class);
//        entity.setCompany(company);
    employeeDAO.insert(entity);
    return entity;
  }

  
  @Override
  public List<String> getAllEmployeeIds() {
    return employeeDAO.findAll().stream()
        .map(employee -> employee.getEmployeeId())
        .collect(Collectors.toList());
  }

}
