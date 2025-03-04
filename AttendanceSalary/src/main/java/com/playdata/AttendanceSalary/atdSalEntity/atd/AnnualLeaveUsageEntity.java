package com.playdata.attendanceSalary.atdSalEntity.atd;

import com.playdata.Common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "annual_leave_usage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualLeaveUsageEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annual_leave_id", nullable = false)
    private Long annualLeaveId;


    @Column(name = "employee_id", nullable = false, length = 50)
    private char employeeId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "회사코드", length = 100)
    private BigDecimal companyCode;





    @Column(name = "id")
    private Long id;

}