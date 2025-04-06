package com.playdata.AttendanceSalary.atdSalDto.sal;


import com.playdata.AttendanceSalary.atdSalEntity.sal.DeductionEntity;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductionResponseDTO {

  //1추가
  private Long deductionId;
  private BigDecimal amount;
  private Long payStubId;
  private String deductionType;
  private String employeeId;

  public DeductionEntity toEntity() {
    DeductionEntity deductionEntity = new DeductionEntity();
    deductionEntity.setDeductionId(this.deductionId);
//        deductionEntity.setPayrollDetailsId(this.payrollDetailsId);
//        deductionEntity.setPayrollItemId(this.payrollItemId);
    deductionEntity.setAmount(this.amount);

    // Service에서 salary, payStub 주입 필요
    return deductionEntity;
  }
}
