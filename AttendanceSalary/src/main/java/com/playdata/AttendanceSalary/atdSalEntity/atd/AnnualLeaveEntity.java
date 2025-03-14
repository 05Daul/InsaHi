package com.playdata.AttendanceSalary.atdSalEntity.atd;

import com.playdata.common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "annual_leave")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualLeaveEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long AnnualLeaveId;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    private Long annualLeaveIdUsageId; //

    @Column(length = 50)
    private String positionName; //직급? 부서?

    private int baseLeave; // 기본 연차

    private int additionalLeave;  //추가 연차

    private int totalGrantedLeave; // 총 연차

    private int remainingLeave; // 남은

    private int usedLeave; //사용 연차

    private String companyCode;

//    @ManyToOne
//    @JoinColumn(name = "employee_id")
//    private Employee employee;

}