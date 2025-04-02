package com.playdata.HumanResourceManagement.employee.dto;

import java.time.LocalDate;
import java.util.Set;
import lombok.Data;

@Data
public class EmpAuthResponseDTO {

  private String employeeId;
  private String password;
  private String name;
  private String role;
  private String companyCode;
  private String email;
  private String phoneNumber;
  private String address;
  private String gender;
  private String birthday;
  private String departmentId;
  private String teamId;
  private String state;
  private Long positionSalaryId;
  private LocalDate hireDate;
  private LocalDate retireDate;
  private Set<String> authorityNames;

}
