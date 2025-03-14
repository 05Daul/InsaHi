package com.playdata.AttendanceSalary.atdSalDto.atd;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnualLeaveRequestDTO {

  private Long annualLeaveUsageId; //
  private Long AnnualLeaveId;
  private LocalDate startDate; //시작일
  private LocalDate stopDate; //종료일
  private String reason; // 사유
  private String leaveApprovalStatus;
  private String companyCode;
  private String employeeId;

  /*
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

}*/

}
