package com.playdata.attendanceSalary.atdSalEntity.sal;

import com.playdata.Common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
//수당
@Entity
@Table(name = "allowance")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class AllowanceEntity extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allowanceId;
    private String companyCode;
    private boolean taxExemption;
    private BigDecimal salary;


    @ManyToOne
    @JoinColumn(name="salary_id")
    private SalaryEntity salaryId;

    @ManyToOne
    @JoinColumn(name="payStub_id")
    private PayStubEntity payStubId;


}
