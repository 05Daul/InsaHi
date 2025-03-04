package com.playdata.attendanceSalary.atdSalEntity.atd;
import com.playdata.Common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "annual_leave")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualLeaveEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "직원ID", nullable = false, length = 50)
    private char employeeId;

    @Column( length = 50)
    private String positionName;

    private int baseLeave;

    private int additionalLeave;

    private int totalGrantedLeave;

    private int remainingLeave;

    private int usedLeave;

    @Column(length = 100)
    private BigDecimal companyCode;

}