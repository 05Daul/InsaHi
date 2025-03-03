package com.playdata.HumanResourceManagement.entity;

import com.playdata.Common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "position_salary_steps")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionSalaryStepEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_salary_step_id")
    private long id;  // 직급호봉 ID (PK)

    @Column(name = "position_id", length = 50)
    private long positionId;  // 직급 ID

    @Column(name = "salary_step")
    private int salaryStep;  // 호봉

    @Column(name = "base_salary", precision = 11, scale = 2)
    private BigDecimal baseSalary;  // 기본급

    @Column(name = "position_allowance", precision = 11, scale = 2)
    private BigDecimal positionAllowance;  // 직책 수당

    @Column(name = "overtime_rate", precision = 5, scale = 2)
    private BigDecimal overtimeRate;  // 연장 근로율

    @Column(name = "base_annual_leave")
    private int baseAnnualLeave;  // 기본 연차

    @Column(name = "salary_increase_allowance", precision = 10, scale = 2)
    private BigDecimal salaryIncreaseAllowance;  // 인상 수당

    @ManyToOne
    @JoinColumn(name = "company_code", referencedColumnName = "companyCode", nullable = false)
    private Company company;  // 회사와의 관계
}
