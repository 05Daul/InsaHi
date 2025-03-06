package com.playdata.attendanceSalary.atdSalEntity.atd;

import com.playdata.User.employee.entity.Employee;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")//출퇴근ID
    private Long id;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;


    @Column(name = "work_date", nullable = false) // 근무일자
    private LocalDate workDate;

    @Column(name = "check_out_time") //퇴근시간
    private LocalDateTime checkOutTime;

    @Column(name = "work_hours", precision = 7, scale = 2) //근무시간
    private BigDecimal workHours;

    @Column(name = "overtime_hours", precision = 7, scale = 2)// 일일연장근로 시간
    private BigDecimal overtimeHours;

    // @Column(name = "총연장근로시간", precision = 5, scale = 2)
    // private BigDecimal totalOvertimeHours;
    // private boolean workWorkingOutside;

    @Column(name = "Attendance_status")
    @Enumerated(EnumType.STRING)
    private AttendanceStauts attendanceStatus;

    private String companyCode;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

/*   @Column(name = "is_late")
    prALTER TABLE employee DROP FOREIGN KEY FK_employee_company;ivate boolean isLate;
*/

/*
    @PrePersist //db에 출근으로  생성시
    public void calculateLateStatus() {
        if (this.checkInTime != null) {
            this.isLate = this.checkInTime.toLocalTime().isAfter(LocalTime.of(9, 0));
        }
        *//**  추후 회사 테이블에서 입력된 시간을 받을 코드  *//*
    }*/
}
