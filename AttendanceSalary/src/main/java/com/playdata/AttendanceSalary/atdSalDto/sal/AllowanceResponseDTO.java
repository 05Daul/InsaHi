package com.playdata.AttendanceSalary.atdSalDto.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.AllowanceEntity;
import com.playdata.AttendanceSalary.atdSalEntity.sal.AllowanceType;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllowanceResponseDTO {

  private Long allowanceId;
  private String companyCode;
  private BigDecimal allowSalary;
  //    private PayStubEntity payStub;
  private String allowName;
  private String allowType;
  private String employeeId;

  public AllowanceEntity toEntity() {
    AllowanceEntity allowanceEntity = new AllowanceEntity();
    allowanceEntity.setAllowanceId(allowanceId);
    allowanceEntity.setAllowanceId(this.allowanceId);
    allowanceEntity.setCompanyCode(this.companyCode);
    allowanceEntity.setAllowSalary(this.allowSalary);
    allowanceEntity.setAllowType(AllowanceType.valueOf(allowType));
    return allowanceEntity;
  }

}
