package com.playdata.AttendanceSalary.atdSalDto.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AttendanceEntity;
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
    private Long id;
    private String employeeId;
    private String companyCode;
    private LocalDate workDate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private BigDecimal workHours;
    private BigDecimal overtimeHours;
    private String attendanceStatus;

    AttendanceEntity toEntity() {
        AttendanceEntity attendance = new AttendanceEntity();
        attendance.setId(id);
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
