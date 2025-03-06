package com.playdata.User.employee.service;

import com.playdata.User.employee.authentication.EmpAuthenticationToken;
import com.playdata.User.employee.dao.AuthorityDAO;
import com.playdata.User.employee.dao.EmployeeDAO;
import com.playdata.User.employee.dto.EmployeeRequestDTO;
import com.playdata.User.employee.dto.LoginDTO;
import com.playdata.User.employee.entity.Authority;
import com.playdata.User.employee.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;
    private final AuthorityDAO authorityDAO;
    private final ModelMapper modelMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    public Employee insertEmployee(EmployeeRequestDTO employeeRequestDTO) {

        Employee entity = modelMapper.map(employeeRequestDTO, Employee.class);
//        entity.setCompany(company);
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
        employee.setAuthoritylist(roles);
        employeeDAO.insert(employee);
    }








    @Override
    public Authentication signin(LoginDTO employee) {
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
}
