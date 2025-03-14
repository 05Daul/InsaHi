package com.playdata.AttendanceSalary.atdSalDto.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.AllowanceType;
import com.playdata.AttendanceSalary.atdSalEntity.sal.EmployeeAllowEntity;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeAllowDTO {
    private Long employeeAllowId;
    private String employeeId;
    private AllowanceType allowanceType;
    private BigDecimal amount;

    EmployeeAllowEntity toEmployeeAllowEntity() {
        EmployeeAllowEntity entity = new EmployeeAllowEntity();
        entity.setEmployeeAllowId(employeeAllowId);
        entity.setEmployeeId(employeeId);
        entity.setAllowanceType(allowanceType);
        return entity;
    }

}
