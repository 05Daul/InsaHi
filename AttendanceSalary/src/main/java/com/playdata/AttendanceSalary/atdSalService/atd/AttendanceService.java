package com.playdata.AttendanceSalary.atdSalService.atd;

import com.playdata.AttendanceSalary.atdSalDto.atd.AttendanceDTO;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AttendanceEntity;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public interface AttendanceService {

  AttendanceDTO checkIn(String employeeId) throws IllegalAccessException;
  void checkOut(Long attendanceId);
  // AttendanceEntity findWorkingOutside(String employeeId, String companyCode);
  BigDecimal calculateMonthlyOvertimeHours(String employeeId, YearMonth yearMonth);
  List<AttendanceDTO> getAttendanceByEmployeeId(String employeeId);
  AttendanceDTO findAttendanceByEmployeeId(String employeeId);
}

