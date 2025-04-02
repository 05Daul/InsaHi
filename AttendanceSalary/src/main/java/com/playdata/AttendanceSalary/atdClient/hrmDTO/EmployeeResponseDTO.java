package com.playdata.AttendanceSalary.atdClient.hrmDTO;


import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

  private LocalDate hireDate;
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
  private LocalTime retireDate;


}

