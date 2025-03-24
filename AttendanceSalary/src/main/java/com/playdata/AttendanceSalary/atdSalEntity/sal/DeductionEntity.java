package com.playdata.AttendanceSalary.atdSalEntity.sal;


import com.playdata.common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/// 공제 항목
@Builder
@Entity
@Table(name = "deduction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeductionEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deduction_id")
    private Long deductionId;

    @Enumerated(EnumType.STRING)
    private DeductionType deductionType;  // 국민연금, 건강보험, 고용보험 등

    @Column(name = "amount", precision = 11, scale = 2)
    private BigDecimal amount; // 개인 부담금 기준

    private BigDecimal deductionAmount;
    // 급여 항목과 급여 명세서 연관관계
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "salary_id")
//    private SalaryEntity salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_stub_id")
    private PayStubEntity payStub;
//
//    private Long payrollDetailsId;
//    private Long payrollItemId;
}

