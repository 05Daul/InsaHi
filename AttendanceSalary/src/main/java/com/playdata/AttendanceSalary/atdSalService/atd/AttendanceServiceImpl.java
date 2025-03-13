package com.playdata.attendanceSalary.atdSalService.atd;

import com.playdata.attendanceSalary.atdClient.HrmFeignClient;
import com.playdata.attendanceSalary.atdSalDao.atd.AttendanceDAO;
import com.playdata.attendanceSalary.atdSalEntity.atd.AttendanceEntity;
import com.playdata.attendanceSalary.atdSalEntity.atd.AttendanceStauts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {
    ///  44번째줄 통신으로 company받아와서 getStartTime
    private final AttendanceDAO attendanceDAO;
    private final HrmFeignClient hrmFeignClient;
    private final ModelMapper modelMapper;


    @Override
    public BigDecimal calculateMonthlyOvertimeHours(String employeeId, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        BigDecimal totalOvertime = attendanceDAO.getTotalOvertimeHoursByEmployeeAndDateRange(employeeId, startDate, endDate);
        log.info("직원 ID: {}의 {}월 연장 근무 시간 합계: {}", employeeId, yearMonth.getMonthValue(), totalOvertime);
        return totalOvertime;
    }

    @Override
    public AttendanceEntity checkIn(String employeeId, String companyCode) throws IllegalAccessException {
        hrmFeignClient.findEmployee(employeeId);


        //        Employee employee = attendanceDAO.findEmployeeById(employeeId);
        // 1) null 처리 후 진행
        AttendanceEntity attendanceEntity = new AttendanceEntity();
        //attendanceEntity.setEmployee(employee)
        attendanceEntity.setEmployeeId(employeeId);

        if (attendanceEntity.getEmployeeId() == null) {
            throw new IllegalAccessException("해당 직원이 없습니다.");
        }
        attendanceEntity.setEmployeeId(employeeId);

        if (attendanceEntity.getCompanyCode() == null) {
            throw new IllegalAccessException("해당 회사가 없습니다.");
        }
        attendanceEntity.setCompanyCode(companyCode);
        // 회사별 출근 시각 지정

        // LocalTime companyHour = employee.getCompany().getStartTime();
        /// 통신으로 company받아와서 getStartTime();
        LocalDate localDate = Objects.requireNonNull(hrmFeignClient.getCompanyStartTime(employeeId).getBody()).toLocalDate();;
        LocalTime companyHour = LocalTime.of(0, 0);

        log.info("company code:{}, company hour:{}", companyCode, companyHour);

        // 내 출근 시간 기본값 설정
        attendanceEntity.setWorkDate(LocalDate.now());

        LocalDateTime todayTime = LocalDateTime.of(LocalDate.now(), companyHour);
        DayOfWeek today = todayTime.getDayOfWeek();
        boolean isWeekday = (today != DayOfWeek.SATURDAY && today != DayOfWeek.SUNDAY);

        // 출근 시간 설정 로직
        LocalDateTime checkInTime = LocalDateTime.now();

        if (LocalTime.now().isBefore(companyHour) && isWeekday) {
            attendanceEntity.setAttendanceStatus(AttendanceStauts.ATTENDANCE);
            attendanceEntity.setCheckInTime(LocalDateTime.of(LocalDate.now(), companyHour));
        } else if (LocalTime.now().isAfter(companyHour) && isWeekday) {
            attendanceEntity.setAttendanceStatus(AttendanceStauts.TARDINESS);
            attendanceEntity.setCheckInTime(checkInTime);
        }
        // 주말 출근 처리
        if (!isWeekday) {
            attendanceEntity.setCheckInTime(checkInTime);
            attendanceEntity.setAttendanceStatus(AttendanceStauts.WEEKEND_WORK);
        }

        // 출근 시간 저장
        return attendanceDAO.save(attendanceEntity);
    }


    public void checkOut(Long id) {
        AttendanceEntity attendance = attendanceDAO.findById(id);

        LocalDateTime now = LocalDateTime.now();
        attendance.setCheckOutTime(now); // 퇴근 시간 기록

        // 근무 시간 계산
        Duration duration = Duration.between(attendance.getCheckInTime(), now);
        long workMinutes = Math.max(0, duration.toMinutesPart());
        long workHours = Math.max(0, duration.toHours());

        // 주말 확인
        DayOfWeek today = now.getDayOfWeek();
        boolean isWeekend = (today == DayOfWeek.SATURDAY || today == DayOfWeek.SUNDAY);

        // 연장 근무 시간 계산 (법정 근무 시간: 8시간)
        long overtimeMinutesTotal = 0;
        if (!isWeekend) {
            if (workHours > 8 || (workHours == 8 && workMinutes > 0)) {
                overtimeMinutesTotal = ((workHours - 8) * 60) + workMinutes;
            }
        } else {
            // 주말은 모두 연장근무 처리
            overtimeMinutesTotal = (workHours * 60) + workMinutes;
        }
//        long overtimeMinutesTotal = 0;
//        if (workHours > 8 || (workHours == 8 && workMinutes > 0) || !isWeekend) {//수정
//             overtimeMinutesTotal = ((workHours - 8) * 60) + workMinutes; // 초과 근무 시간을 분 단위로 변환
//        }
//
//        // 주말 일시 모두 연장 근무 시간으로 더하기
//        if (isWeekend) {
//            overtimeMinutesTotal += (workHours * 60) + workMinutes;
//        }
        // DB에서 기존 연장 근무 시간 불러오기. // 수정
//        BigDecimal overtimeDB = attendance.getOvertimeHours() != null ? attendance.getOvertimeHours() : BigDecimal.ZERO;

        BigDecimal todayOvertimeWorkHours = BigDecimal.valueOf(overtimeMinutesTotal)
                .divide(BigDecimal.valueOf(60), 3, RoundingMode.HALF_UP); // 소수점 3자리 반올림

        /*// 총 연장 근로 시간 계산
        BigDecimal totalOvertimeWorkTime = overtimeDB.add(todayOvertimeWorkHours);
*/
        // 근무 시간 및 연장 근무 시간 저장
        attendance.setWorkHours(BigDecimal.valueOf(duration.toMinutes()).max(BigDecimal.ZERO).divide(BigDecimal.valueOf(60), 3, RoundingMode.HALF_UP)); // 총 근무 시간 (분 단위)
        attendance.setOvertimeHours(todayOvertimeWorkHours);

        // 데이터 저장
        attendanceDAO.save(attendance);
    }
}
