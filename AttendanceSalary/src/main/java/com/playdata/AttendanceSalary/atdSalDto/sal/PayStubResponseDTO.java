package com.playdata.AttendanceSalary.atdSalDto.sal;


import com.playdata.AttendanceSalary.atdSalEntity.sal.PayStubEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PayStubResponseDTO {
    private Long payStubId;
    private LocalDateTime paymentDate;
    private BigDecimal baseSalary;
    private BigDecimal totalAllowances;
    private BigDecimal overtimePay;
    private BigDecimal totalPayment;
    private BigDecimal totalDeductions;
    private BigDecimal netPay;
    private String companyCode;
    private String employeeId;

    public PayStubEntity toEntity() {
        PayStubEntity payStubEntity = new PayStubEntity();
        payStubEntity.setPayStubId(payStubId);
        payStubEntity.setPaymentDate(paymentDate);
        payStubEntity.setBaseSalary(baseSalary);
        payStubEntity.setTotalAllowances(totalAllowances);
        payStubEntity.setOvertimePay(overtimePay);
        payStubEntity.setTotalPayment(totalPayment);
        payStubEntity.setTotalDeductions(totalDeductions);
        payStubEntity.setNetPay(netPay);
        payStubEntity.setCompanyCode(companyCode);
        payStubEntity.setEmployeeId(employeeId);
        return payStubEntity;
    }
}
