package com.playdata.attendanceSalary.atdSalService.atd;


import com.playdata.attendanceSalary.atdSalEntity.atd.AttendanceEntity;
import java.math.BigDecimal;
import java.time.YearMonth;

public interface AttendanceService {
    AttendanceEntity checkIn(String employeeId,String companyCode) throws IllegalAccessException;
    void checkOut(Long attendanceId);
    // AttendanceEntity findWorkingOutside(String employeeId, String companyCode);
    BigDecimal calculateMonthlyOvertimeHours(String employeeId, YearMonth yearMonth);

}

