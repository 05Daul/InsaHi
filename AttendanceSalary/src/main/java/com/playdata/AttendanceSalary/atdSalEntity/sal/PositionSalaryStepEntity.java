package com.playdata.attendanceSalary.atdSalEntity.sal;

import com.playdata.Common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
/// 직급호봉
@Entity
@Table(name = "position_salary_step")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionSalaryStepEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_salary_id")
    private Long id;

    @Column(name = "position_id", length = 50)
    private Long positionId;

    @Column(name = "salary_step_id")
    private int salaryStepId;

    @Column(precision = 11, scale = 2)
    private BigDecimal baseSalary;

    @Column(precision = 11, scale = 2)
    private BigDecimal positionAllowance;

    @Column(precision = 5, scale = 2)
    private BigDecimal overtimeRate;

    private int baseAnnualLeave;

    @Column(precision = 10, scale = 2)
    private BigDecimal salaryIncreaseAllowance;


    private String companyCode;

}