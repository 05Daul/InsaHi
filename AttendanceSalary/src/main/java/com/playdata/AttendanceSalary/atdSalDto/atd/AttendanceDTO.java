package com.playdata.AttendanceSalary.atdSalDto.atd;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
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
}
