package com.playdata.User.employee.dto;

import com.playdata.User.company.entity.Company;
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
//    private String role; //enum
    private String companyCode;
    private String email;
    private String phoneNumber;
//    private String address;
//    private String gender;
//    private String birthday;
//    private String departmentId;
//    private String teamId;
//    private String state;

}
