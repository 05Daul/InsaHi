package com.playdata.attendanceSalary.atdSalEntity.sal;

import com.playdata.Common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "position_salary_step")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PositionSalaryStepEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_salary_id")
    private long id;

    @Column(name = "position_id", length = 50)
    private long positionId;

    @Column(name = "salary_step_id")
    private int salaryStepId;

    @Column( precision = 11, scale = 2)
    private BigDecimal baseSalary;

    @Column( precision = 11, scale = 2)
    private BigDecimal positionAllowance;

    @Column( precision = 5, scale = 2)
    private BigDecimal overtimeRate;

    private int baseAnnualLeave;

    @Column( precision = 10, scale = 2)
    private BigDecimal salaryIncreaseAllowance;

    @Column(name = "회사코드", length = 100)
    private BigDecimal companyCode;
}