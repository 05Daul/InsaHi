package com.playdata.attendanceSalary.atdSalEntity.atd;
import com.playdata.attendanceSalary.test.TestEmployee;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor


public class AttendanceEntity {
    /** 회사테이블의 정해지 시간 대신 (추후 삭제 예정)   */
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id")//출퇴근ID
    private Long id;

    @Column(name = "employee_id", nullable = false, length = 50) //직원 id
    private String employeeId;

    @Column(name = "work_date", nullable = false) // 근무일자
    private LocalDate workDate;

     @Column(name = "check_out_time") //퇴근시간
    private LocalDateTime checkOutTime;

   @Column(name = "work_hours", precision = 5, scale = 2) //근무시간
    private BigDecimal workHours;

    @Column(name = "overtime_hours", precision = 5, scale = 2)// 일일연장근로 시간
    private BigDecimal overtimeHours;

    // @Column(name = "총연장근로시간", precision = 5, scale = 2)
    // private BigDecimal totalOvertimeHours;

    @Column(name = "company_code", length = 100)
    private String companyCode;

     @Column(name = "is_late")
    private boolean isLate;

    @OneToOne(cascade = CascadeType.ALL)
    TestEmployee testEmployee;
    // private boolean workWorkingOutside;

    @Column(name="Attendance_status")
    @Enumerated(EnumType.STRING)
    private AttendanceStauts attendanceStatus;

    @PrePersist //db에 출근으로  생성시
    public void calculateLateStatus() {
        if (this.checkInTime != null) {
            this.isLate = this.checkInTime.toLocalTime().isAfter(LocalTime.of(9, 0));
        }
            /**  추후 회사 테이블에서 입력된 시간을 받을 코드  */
    }


}
