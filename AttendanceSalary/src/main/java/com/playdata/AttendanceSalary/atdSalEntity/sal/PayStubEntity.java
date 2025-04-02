package com.playdata.AttendanceSalary.atdSalEntity.sal;

import com.playdata.AttendanceSalary.atdSalDto.sal.PayStubResponseDTO;
import com.playdata.common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/// 급여 명세
@Entity
@Table(name = "pay_stub")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayStubEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_stub_id")
    private Long payStubId;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "base_salary", precision = 11, scale = 2)
    private BigDecimal baseSalary;

    @Column(name = "total_allowances", precision = 11, scale = 2)
    private BigDecimal totalAllowances;

    @Column(precision = 11, scale = 2)
    private BigDecimal overtimePay; /// 연장 수당

    @Column(precision = 11, scale = 2)
    private BigDecimal totalPayment; /// 총 급여

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "salary_id")
//    private SalaryEntity salary;
    private BigDecimal totalTaxFreeAllowances;
    @Column(precision = 11, scale = 2)
    private BigDecimal totalDeductions; ///총공제

    @Column(precision = 11, scale = 2)
    private BigDecimal netPay; ///실 급여
    private String companyCode;

    @Column(name = "employee_id")
    private String employeeId;
    @OneToMany(mappedBy = "payStub", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AllowanceEntity> allowances = new ArrayList<>();
    @OneToMany(mappedBy = "payStub", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DeductionEntity> deductions = new ArrayList<>();

    public PayStubResponseDTO toPayStubResponseDTO() {
        PayStubResponseDTO payStubResponseDTO = new PayStubResponseDTO();
        payStubResponseDTO.setPayStubId(payStubId);
        payStubResponseDTO.setPaymentDate(paymentDate);
        payStubResponseDTO.setBaseSalary(baseSalary);
        payStubResponseDTO.setTotalAllowances(totalAllowances);
        payStubResponseDTO.setOvertimePay(overtimePay);
        payStubResponseDTO.setTotalPayment(totalPayment);
        payStubResponseDTO.setTotalDeductions(totalDeductions);
        payStubResponseDTO.setNetPay(netPay);
        payStubResponseDTO.setCompanyCode(companyCode);
        payStubResponseDTO.setEmployeeId(employeeId);
        return payStubResponseDTO;
    }


}