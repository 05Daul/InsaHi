package com.playdata.AttendanceSalary.atdSalDao.atd;

import com.playdata.AttendanceSalary.atdSalDto.atd.AttendanceDTO;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AttendanceEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface AttendanceDAO {
    AttendanceEntity save(AttendanceEntity attendance);

    AttendanceEntity findById(Long id);

    BigDecimal getTotalOvertimeHoursByEmployeeAndDateRange(String employeeId, LocalDate startDate, LocalDate endDate);

    List<AttendanceEntity> findByEmployeeId(String employeeId);
    Optional<AttendanceEntity> findIdByEmployeeId(String employeeId);
}

