package com.playdata.HumanResourceManagement.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCardDTO {
    private String employeeId;
    private String name;
    private String phoneNumber;
    private String departmentId;
    private String positionSalaryId;


}
