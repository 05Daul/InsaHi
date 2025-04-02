package com.playdata.HumanResourceManagement.company.dto;

import com.playdata.HumanResourceManagement.employee.dto.AdminRequestDTO;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {

  //company
  private String companyCode;
  private String companyName;
  private String companyAddress;
  private String headCount;
  private Date createdAt;
  private String businessNumber;
  //employee
  private String employeeId;
  private String password;
  private String name;
  private String email;
  private String phoneNumber;
  private String departmentId;

  public CompanyRequestDTO companySignupRequestDTO() {
    return new CompanyRequestDTO(companyCode, companyName, companyAddress, headCount, createdAt,
        businessNumber);
  }

  public AdminRequestDTO AdminSignupRequestDTO() {
    return new AdminRequestDTO(employeeId, password, name, companyCode, departmentId, email,
        phoneNumber);
  }
}
