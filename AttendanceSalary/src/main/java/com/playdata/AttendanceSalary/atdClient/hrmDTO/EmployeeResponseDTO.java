package com.playdata.AttendanceSalary.atdClient.hrmDTO;


import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

  private String employeeId;
  private String employeeName;
  private Long positionSalaryId;   // 포지션 ID
  private String companyCode; // 회사 코드
  private LocalDate hireDate;
//    private String name;
//    private String role; //enum
//    private String email;
    private String name;
    private String role; //enum
    private String email;
//    private String phoneNumber;
//    private String address;
//    private String gender;
//    private String birthday;
    private String departmentId;
    private String teamId;
    private String state;

}
