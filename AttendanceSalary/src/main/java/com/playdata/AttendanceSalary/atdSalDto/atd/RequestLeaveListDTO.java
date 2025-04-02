package com.playdata.AttendanceSalary.atdSalDto.atd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestLeaveListDTO {

  private String companyCode;
  private String employeeId;
  private String status;

}
