package com.playdata.AttendanceSalary.atdSalDto.sal;


import com.playdata.AttendanceSalary.atdSalEntity.sal.DeductionEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeductionResponseDTO {
    private Long deductionId;
    private Long payrollDetailsId;
    private Long payrollItemId;
    private BigDecimal amount;
    private Long salaryId;
    private Long payStubId;

    public DeductionEntity toEntity(){
        DeductionEntity deductionEntity = new DeductionEntity();
        deductionEntity.setDeductionId(this.deductionId);
//        deductionEntity.setPayrollDetailsId(this.payrollDetailsId);
//        deductionEntity.setPayrollItemId(this.payrollItemId);
        deductionEntity.setAmount(this.amount);

        // Service에서 salary, payStub 주입 필요
        return deductionEntity;
}
}
