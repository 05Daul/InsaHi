package com.playdata.HumanResourceManagement.employee.service;

import com.playdata.HumanResourceManagement.employee.authentication.EmpAuthenticationToken;
import com.playdata.HumanResourceManagement.employee.dao.AuthorityDAO;
import com.playdata.HumanResourceManagement.employee.dao.EmployeeDAO;
import com.playdata.HumanResourceManagement.employee.dto.AdminRequestDTO;
import com.playdata.HumanResourceManagement.employee.dto.AuthorityResponseDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmpAuthResponseDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeRequestDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeResponseDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeUpdateDTO;
import com.playdata.HumanResourceManagement.employee.dto.LoginDTO;
import com.playdata.HumanResourceManagement.employee.dto.ProfileCardDTO;
import com.playdata.HumanResourceManagement.employee.dto.UpdatePasswordDTO;
import com.playdata.HumanResourceManagement.employee.entity.Authority;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import com.playdata.HumanResourceManagement.employee.repository.EmployeeRepository;
import com.playdata.HumanResourceManagement.employee.repository.FileEntityRepository;
import com.playdata.HumanResourceManagement.publicEntity.FileEntity;
import jakarta.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  @Value("${file.upload-dir}")
  private String uploadDir;

  @Value("${file.access-url}")
  private String accessUrl;


  private final EmployeeDAO employeeDAO;
  private final AuthorityDAO authorityDAO;
  private final ModelMapper modelMapper;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final PasswordEncoder passwordEncoder;
  private final EmployeeRepository employeeRepository;
  private final FileEntityRepository fileEntityRepository;


  @Override
  // 직급 호봉 업데이트
  public void updateEmployee(String employeeId, Long positionSalaryId) {
    //인원을 찾고
    Employee employee = employeeDAO.findByEmployeeId(employeeId);
    employee.setPositionSalaryId(positionSalaryId);
    employeeDAO.insert(employee);
  }

  @Override
  public List<EmpAuthResponseDTO> findByAuthorityList_AuthorityName(String authorityName) {
    List<Employee> employees = employeeRepository.findByAuthorityList_AuthorityName(authorityName);
    return employees.stream().map(e -> modelMapper.
            map(e, EmpAuthResponseDTO.class))
        .collect(Collectors.toList());
  }

  @Override
  public void addAdminRoleToEmployee(String employeeId) {
    employeeDAO.addAdminRoleToEmployee(employeeId);
  }

  @Override
  public void removeAdminRoleFromEmployee(String employeeId) {
    employeeDAO.removeAdminRoleFromEmployee(employeeId);
  }

  @Override
  public List<AuthorityResponseDTO> findAuthoritiesByCompanyCode(String companyCode) {
    List<Authority> authorities = employeeDAO.findAuthoritiesByCompanyCode(companyCode);
    return authorities.stream().map(
        authority -> modelMapper
            .map(authority, AuthorityResponseDTO.class)).collect(Collectors.toList());
  }

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
    return modelMapper.map(employee, EmployeeResponseDTO.class);
  }

  @Override
  /// 김다울 추가
  public LocalTime findCompanyStartTimeByEmployeeId(String employeeId) {
    return employeeDAO.findCompanyStartTimeByEmployeeId(employeeId);
  }


  @Override
  public List<String> getAllEmployeeIds() {
    return employeeDAO.findAll().stream()
        .map(employee -> employee.getEmployeeId())
        .collect(Collectors.toList());


  }

  //mypage 왼쪽 작은 프로필
  @Override
  public ProfileCardDTO getProfileCard(String employeeId) {
    Employee employee = employeeDAO.findByEmployeeId(employeeId);
    if (employee == null) {
      throw new EntityNotFoundException("해당 직원이 존재하지 않습니다: " + employeeId);
    }

    ProfileCardDTO dto = new ProfileCardDTO();

    dto.setEmployeeId(employee.getEmployeeId());
    dto.setName(employee.getName());
    dto.setPhoneNumber(employee.getPhoneNumber());
    dto.setDepartmentName(employee.getDepartment().getDepartmentName());
    dto.setEmail(employee.getEmail());
    dto.setHireDate(employee.getHireDate());

    if (employee.getProfileImage() != null) {
      String imageUrl =
          uploadDir + employee.getProfileImage().getStoreFilename();
      dto.setProfileImage(imageUrl);
    }

    return dto;
  }

  //개인정보수정페이지
  @Override
  public EmployeeResponseDTO getEmployeeInfo(String employeeId) {
    Employee employee = employeeDAO.findByEmployeeId(employeeId);
    EmployeeResponseDTO responseDTO = new EmployeeResponseDTO();
    responseDTO.setEmployeeId(employee.getEmployeeId());
    responseDTO.setName(employee.getName());
    responseDTO.setPhoneNumber(employee.getPhoneNumber());
    responseDTO.setDepartmentName(employee.getDepartment().getDepartmentName());
    responseDTO.setEmail(employee.getEmail());
    responseDTO.setHireDate(employee.getHireDate());

    if (employee.getProfileImage() != null) {
      String imageUrl =
          uploadDir + employee.getProfileImage().getStoreFilename();
      responseDTO.setProfileImage(imageUrl);
    }

    return responseDTO;
  }

  //개인정보 변경
  @Override
  public EmployeeResponseDTO updateEmployeeInfo(String employeeId,
      EmployeeUpdateDTO updateDTO, MultipartFile profileImage) {
    Employee employee = employeeDAO.findByEmployeeId(employeeId);
    modelMapper.map(updateDTO, employee);

    // 이미지 있을 경우 저장 처리
    if (profileImage != null && !profileImage.isEmpty()) {
      String storedFilename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
      String fullPath = uploadDir + storedFilename;

      try {
        File targetFile = new File(fullPath);
        if (!targetFile.getParentFile().exists()) {
          targetFile.getParentFile().mkdirs();
        }

        profileImage.transferTo(targetFile);

        FileEntity fileEntity = employee.getProfileImage();

        if (fileEntity == null) {
          fileEntity = new FileEntity();
          fileEntity.setEmployee(employee);
        }

        fileEntity.setOriginalFileName(profileImage.getOriginalFilename());
        fileEntity.setStoreFilename(storedFilename);
        fileEntity.setFilePath("/uploads/profile/");
        fileEntity.setCategory("employee");

        employee.setProfileImage(fileEntity); // 새로 할당 or 갱신된 상태

      } catch (IOException e) {
        throw new RuntimeException("파일 저장 실패", e);
      }
    }

    employeeDAO.update(employee);
    EmployeeResponseDTO employeeResponseDTO = modelMapper.map(employee, EmployeeResponseDTO.class);

    return employeeResponseDTO;
  }

  //비밀번호 변경
  @Override
  public EmployeeResponseDTO updatePassword(String employeeId,
      UpdatePasswordDTO updatePasswordDTO) {
    Employee employee = employeeDAO.findById(employeeId);
    if (!passwordEncoder.matches(updatePasswordDTO.getCurrentPassword(), employee.getPassword())) {
      throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
    }
    String encodedNewPassword = passwordEncoder.encode(updatePasswordDTO.getNewPassword());
    employee.setPassword(encodedNewPassword);

    employeeDAO.update(employee);
    EmployeeResponseDTO employeeResponseDTO = modelMapper.map(employee, EmployeeResponseDTO.class);
    return employeeResponseDTO;
  }

  //user 권한만 추가
  @Override
  public void addUserRoles(Employee employee) {
    Set<Authority> roles = new HashSet<>();

    authorityDAO.getUserRole().ifPresent(roles::add);

    employee.setAuthorityList(roles);
    employeeDAO.insert(employee);
  }

  //회원 등록
  @Override
  public Employee employeeInsert(EmployeeRequestDTO employeeRequestDTO) {
    Employee entity = modelMapper.map(employeeRequestDTO, Employee.class);

    entity.setPassword(passwordEncoder.encode(entity.getPassword()));
    employeeDAO.insert(entity);
    return entity;
  }

  @Override
  public List<Employee> getMemberList() {
    return employeeRepository.findAll();
  }

}

