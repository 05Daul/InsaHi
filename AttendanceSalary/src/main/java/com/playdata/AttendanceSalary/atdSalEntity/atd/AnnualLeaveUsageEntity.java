package com.playdata.AttendanceSalary.atdSalEntity.atd;

import com.playdata.common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.*;

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