package com.playdata.HumanResourceManagement.company.dto;

import com.playdata.HumanResourceManagement.employee.dto.EmployeeRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {
    private CompanyRequestDTO company;
    private EmployeeRequestDTO employee;
}
