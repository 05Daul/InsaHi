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
@Builder
public class AnnualLeaveUsageEntity extends DateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long annualLeaveUsageId; //

  private Long annualLeaveId;

  @Column(nullable = false)
  private LocalDate startDate; //시작일

  @Column(nullable = false)
  private LocalDate stopDate; //종료일

  @Column(length = 255)
  private String reason; // 사유

  @Enumerated(EnumType.STRING)
  private LeaveApprovalStatus leaveApprovalStatus;

  private String employeeId;

  private String companyCode;


}