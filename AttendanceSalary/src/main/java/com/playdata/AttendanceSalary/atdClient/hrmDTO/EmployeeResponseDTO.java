package com.playdata.AttendanceSalary.atdClient.hrmDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private String employeeId;
    private String employeeName;
    private String companyCode; // 회사 코드
    private String name;
    private String role; //enum
    private String email;
    private  Long  positionSalaryId;
//    private String phoneNumber;
//    private String address;
//    private String gender;
//    private String birthday;
    private String departmentId;
    private String teamId;
    private String state;

}
