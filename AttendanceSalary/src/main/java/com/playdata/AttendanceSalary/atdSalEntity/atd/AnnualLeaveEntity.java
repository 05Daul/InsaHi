package com.playdata.AttendanceSalary.atdSalEntity.atd;

import com.playdata.common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "annual_leave")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnualLeaveEntity extends DateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long annualLeaveId;

  private int baseLeave; // 기본 연차

  private int additionalLeave;  //추가 연차

  private int totalGrantedLeave; // 총 연차

  private int remainingLeave; // 남은

  private int usedLeave; //사용 연차

  private String companyCode;

  private String employeeId;

}