package com.playdata.AttendanceSalary.atdSalDto.sal;


import com.playdata.AttendanceSalary.atdSalEntity.sal.DeductionEntity;
import com.playdata.AttendanceSalary.atdSalEntity.sal.DeductionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductionResponseDTO {
    private Long deductionId;
    private BigDecimal amount;
    private Long payStubId;
    private String deductionType;

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
