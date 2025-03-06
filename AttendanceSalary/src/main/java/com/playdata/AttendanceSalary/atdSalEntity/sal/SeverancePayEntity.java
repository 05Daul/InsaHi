package com.playdata.attendanceSalary.atdSalEntity.sal;

import com.playdata.Common.publicEntity.DateEntity;
import com.playdata.User.employee.entity.Employee;
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
    /// 퇴직금

    private String companyCode;

    @Id
    private String empCode;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;


    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate retirementDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal severanceAmount;


}