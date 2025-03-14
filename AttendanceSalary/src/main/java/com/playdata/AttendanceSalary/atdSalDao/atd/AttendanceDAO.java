package com.playdata.AttendanceSalary.atdSalDao.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AttendanceEntity;

import java.math.BigDecimal;
import java.time.LocalDate;



public interface AttendanceDAO {
    AttendanceEntity save(AttendanceEntity attendance);
    AttendanceEntity findById(Long id);
    BigDecimal getTotalOvertimeHoursByEmployeeAndDateRange(String employeeId, LocalDate startDate, LocalDate endDate);



}

