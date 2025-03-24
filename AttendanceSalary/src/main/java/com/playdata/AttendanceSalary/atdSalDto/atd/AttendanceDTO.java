package com.playdata.AttendanceSalary.atdSalDto.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AttendanceEntity;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AttendanceStauts;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDTO {

    private Long attendanceId;
    private LocalDateTime checkInTime;
    private LocalDate workDate;
    private LocalDateTime checkOutTime;
    private BigDecimal workHours;
    private BigDecimal overtimeHours;
    // @Column(name = "총연장근로시간", precision = 5, scale = 2)
    // private BigDecimal totalOvertimeHours;
    // private boolean workWorkingOutside;
    private AttendanceStauts attendanceStatus;
    private String companyCode;
    private String employeeId;


    AttendanceEntity toEntity() {
        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setAttendanceId(attendanceId);
        attendance.setEmployeeId(employeeId);
        attendance.setCompanyCode(companyCode);
        attendance.setWorkDate(workDate);
        attendance.setCheckInTime(checkInTime);
        attendance.setCheckOutTime(checkOutTime);
        attendance.setWorkHours(workHours);
        attendance.setOvertimeHours(overtimeHours);
        return attendance;
    }

}
