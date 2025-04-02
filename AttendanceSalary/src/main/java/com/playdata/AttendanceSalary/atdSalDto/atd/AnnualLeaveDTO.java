package com.playdata.AttendanceSalary.atdSalDto.atd;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualLeaveDTO {

  private Long annualLeaveId;
  private int baseLeave; // 기본 연차
  private int additionalLeave;  //추가 연차
  private int totalGrantedLeave; // 총 연차
  private int remainingLeave; // 남은
  private int usedLeave; //사용 연차
  private String employeeId;
  private String companyCode;

}
