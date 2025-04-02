package com.playdata.AttendanceSalary.atdSalDto.sal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddPersonPositionSalaryDTO {

  private Long positionSalaryId;
  private String positionName;
  private int salaryStepId;
}
