package com.playdata.HumanResourceManagement.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequestDTO {
    private String employeeId;
    private String password;
    private String name;
    private String companyCode;
    private String email;
    private String phoneNumber;

    private String teamId;
    private String positionSalaryId;
    private String departmentId;

}