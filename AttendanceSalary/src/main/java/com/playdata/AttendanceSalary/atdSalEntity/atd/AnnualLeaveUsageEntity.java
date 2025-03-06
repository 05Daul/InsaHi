package com.playdata.attendanceSalary.atdSalEntity.atd;

import com.playdata.Common.publicEntity.DateEntity;
import com.playdata.User.company.entity.Company;
import com.playdata.User.employee.entity.Employee;
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
    @Column( nullable = false)
    private Long annualLeaveIdUsageId; //

    @Column( nullable = false)
    private LocalDate startDate; //시작일

    @Column( nullable = false)
    private LocalDate stopDate; //종료일


    @Column( length = 255)
    private String reason; // 사유


    private String companyCode;
//
//    @ManyToOne
//    @JoinColumn(name = "employee_id")
//    private Employee employee;
//
//    @Column(name = "id")
//    private Long id;

}