package com.playdata.AttendanceSalary.atdSalService.atd;

import com.playdata.AttendanceSalary.atdClient.HrmFeignClient;
import com.playdata.AttendanceSalary.atdClient.hrmDTO.EmployeeResponseDTO;
import com.playdata.AttendanceSalary.atdSalDao.atd.AttendanceDAO;
import com.playdata.AttendanceSalary.atdSalDto.atd.AttendanceDTO;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AttendanceEntity;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AttendanceStauts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {
    ///  44번째줄 통신으로 company받아와서 getStartTime
    private final AttendanceDAO attendanceDAO;
    private final HrmFeignClient hrmFeignClient;
    private final ModelMapper modelMapper;

//
//    @Override
//    public AttendanceDTO findAttendanceByEmployeeId(String employeeId) {
//        AttendanceEntity attendance = attendanceDAO.findAttendanceByEmployeeId(employeeId).orElseThrow(()
//                -> new RuntimeException("서비스 널 오류"));
//        log.info(attendance.toString());
//        return attendance.toAttendanceDTO();
//    }

    @Override
    public AttendanceDTO findAttendanceByEmployeeId(String employeeId) {
        AttendanceEntity attendance = attendanceDAO.findAttendanceByEmployeeId(employeeId).orElseThrow(()
                -> new RuntimeException("서비스 널 오류"));

        //        log.info(attendance.toString());

        return modelMapper.map(attendance, AttendanceDTO.class);
    }

    @Override
    public List<AttendanceDTO> getAttendanceByEmployeeId(String employeeId) {
        List<AttendanceEntity> entities = attendanceDAO.findByEmployeeId(employeeId);

        // Entity -> DTO 변환
        List<AttendanceDTO> dtoList = entities.stream()
                .map(AttendanceEntity::toAttendanceDTO)
                .toList();

        return dtoList;
    }

    @Override
    public BigDecimal calculateMonthlyOvertimeHours(String employeeId, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        BigDecimal totalOvertime = attendanceDAO.getTotalOvertimeHoursByEmployeeAndDateRange(employeeId, startDate, endDate);
        log.info("직원 ID: {}의 {}월 연장 근무 시간 합계: {}", employeeId, yearMonth.getMonthValue(), totalOvertime);
        return totalOvertime;
    }

    @Override
    public AttendanceDTO checkIn(String employeeId) throws IllegalAccessException {

        EmployeeResponseDTO emp = hrmFeignClient.findEmployee(employeeId);

        // 1) null 처리 후 진행
        if (!attendanceDAO.findCheckOutTimeByEmployeeId(employeeId).isEmpty()) {
            // 값이 없는 경우(null)
            // 퇴근 시간이 없으면 예외를 발생시켜 퇴근 처리하도록 안내
            throw new RuntimeException("퇴근 처리가 되지 않았습니다. 퇴근을 먼저 해주세요.");
        }

        AttendanceEntity attendanceEntity = new AttendanceEntity();
        attendanceEntity.setEmployeeId(emp.getEmployeeId());
        attendanceEntity.setCompanyCode(emp.getCompanyCode());
        // 회사별 출근 시각 지정

        // LocalTime companyHour = employee.getCompany().getStartTime();
        /// 통신으로 company받아와서 getStartTime();
        LocalTime companyHour = hrmFeignClient.getCompanyStartTime(employeeId).getBody();
        System.out.println("서비스단 companyHour = " + companyHour);

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
        return modelMapper.map(attendanceDAO.save(attendanceEntity), AttendanceDTO.class);
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

        BigDecimal todayWorkHours = BigDecimal.valueOf(workMinutes).add(BigDecimal.valueOf(workHours*60));

        BigDecimal todayOvertimeWorkHours = BigDecimal.valueOf(overtimeMinutesTotal)
                .divide(BigDecimal.valueOf(60), 3, RoundingMode.HALF_UP); // 소수점 3자리 반올림
        // 근무 시간 및 연장 근무 시간 저장
        attendance.setWorkHours(todayWorkHours); // 총 근무 시간 (분 단위)
        attendance.setOvertimeHours(todayOvertimeWorkHours);

        // 데이터 저장
        attendanceDAO.save(attendance);
    }
}
