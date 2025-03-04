package com.playdata.attendanceSalary.atdSalEntity.sal;

import com.playdata.Common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "pay_stub")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayStubEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_stub_id")
    private long payStubId;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    @Column(name = "base_salary", precision = 11, scale = 2)
    private BigDecimal baseSalary;

    @Column(name = "total_allowances", precision = 11, scale = 2)
    private BigDecimal totalAllowances;

    @Column(precision = 11, scale = 2)
    private BigDecimal overtimePay;

    @Column(precision = 11, scale = 2)
    private BigDecimal totalPayment;

    @Column( precision = 11, scale = 2)
    private BigDecimal totalDeductions;

    @Column( precision = 11, scale = 2)
    private BigDecimal netPay;
    @Column( precision = 10, scale = 2)

    private BigDecimal companyCode;

    @Column(name = "유저_id")
    private char EmployeeId;
}
