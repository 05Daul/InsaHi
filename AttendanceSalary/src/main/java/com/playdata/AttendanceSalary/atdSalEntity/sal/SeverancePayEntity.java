package com.playdata.AttendanceSalary.atdSalEntity.sal;

import com.playdata.common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
/// 퇴직금
@Entity
@Table(name = "severance_pay")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeverancePayEntity extends DateEntity {
    /// 퇴직금 추후
    private String companyCode;

    @Id
    private String empCode;
    @Column(name = "employee_id")
    private String employeeId;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate retirementDate;
    @Column(precision = 10, scale = 2)
    private BigDecimal severanceAmount;
}