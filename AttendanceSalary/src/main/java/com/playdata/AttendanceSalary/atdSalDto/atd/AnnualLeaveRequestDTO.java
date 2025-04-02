package com.playdata.AttendanceSalary.atdSalDto.atd;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnualLeaveRequestDTO {

  private Long annualLeaveUsageId; //
  private Long annualLeaveId;
  private LocalDate startDate; //시작일
  private LocalDate stopDate; //종료일
  private String reason; // 사유
  private String leaveApprovalStatus;
  private String companyCode;
  private String employeeId;
}
