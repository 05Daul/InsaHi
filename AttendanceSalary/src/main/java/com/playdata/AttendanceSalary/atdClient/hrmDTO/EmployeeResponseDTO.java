package com.playdata.AttendanceSalary.atdClient.hrmDTO;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

  private String name;
  private String employeeId;
  private String role;
  private String companyCode;
  @JsonDeserialize(using = StringToLongDeserializer.class)
  private Long positionSalaryId;
  private Date hireDate;


}

