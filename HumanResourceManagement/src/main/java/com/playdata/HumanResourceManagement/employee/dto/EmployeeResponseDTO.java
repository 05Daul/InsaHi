package com.playdata.HumanResourceManagement.employee.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

    private String employeeId;
    private LocalTime startTime;
    private String password; // 1(default 1234) 비밀번호
    private String name; //2 이름
    private String email; //3 이메일
    private String phoneNumber; //4 전화번호
    private String address; //5 주소
    private String departmentId; // 부서
    private String teamId;
    private String state; // 상태 (Active, Inactive 등)
    private Long positionSalaryId; //직급호봉
    private String companyCode;
    private LocalDate hireDate;
    private LocalDate retireDate;
}
