package com.playdata.attendanceSalary.atdSalEntity.sal;


import com.playdata.Common.publicEntity.DateEntity;

import com.playdata.User.company.entity.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/// 공제 항목
@Entity
@Table(name = "deduction")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class DeductionEntity extends DateEntity {
//공제
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deduction_id")
    private Long deductionId;

    @Column(name = "payroll_details_id", nullable = false)
    private Long payrollDetailsId;

    @Column(name = "payrollItem_id", nullable = false)
    private Long payrollItemId;

    @Column(name = "amount", precision = 11, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "salary_id")
    private SalaryEntity salaryId;

    @ManyToOne
    @JoinColumn(name="payStub_id")
    private PayStubEntity payStubId;


}