package com.playdata.AttendanceSalary.atdSalDto.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.AllowanceEntity;
import com.playdata.AttendanceSalary.atdSalEntity.sal.PayStubEntity;
//import com.playdata.attendanceSalary.atdSalEntity.sal.SalaryEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AllowanceResponseDTO {
    private Long allowanceId;
    private String companyCode;
    private BigDecimal allowSalary;
//    private Long salaryId;
    private PayStubEntity payStub;
    private String allowName;

    public AllowanceEntity toEntity(){
        AllowanceEntity allowanceEntity = new AllowanceEntity();
        allowanceEntity.setAllowanceId(allowanceId);
        allowanceEntity.setAllowanceId(this.allowanceId);
        allowanceEntity.setCompanyCode(this.companyCode);
        allowanceEntity.setAllowSalary(this.allowSalary);
//        allowanceEntity.setSalaryId(this.salaryId);
        return allowanceEntity;
    }

}
