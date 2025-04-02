package com.playdata.AttendanceSalary.atdSalDto.sal;
// import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionEntity;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PositionSalaryStepResponseDTO {

  private Long positionSalaryId;//직급 호봉 아이디
  private Long positionId; // 직급 아이디 (엔티티가 아닌 ID만 사용)
  private int salaryStepId; // 호봉
  private BigDecimal baseSalary; //기본금
  private BigDecimal positionAllowance; //직급 수당
  private BigDecimal overtimeAllowance; // 연장수당
  private int baseAnnualLeave; //기본 연차
  private String companyCode;


}
