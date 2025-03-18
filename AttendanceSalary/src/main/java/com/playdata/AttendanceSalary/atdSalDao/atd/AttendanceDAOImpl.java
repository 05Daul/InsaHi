package com.playdata.AttendanceSalary.atdSalDao.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AttendanceEntity;
import com.playdata.AttendanceSalary.atdSalRepository.atd.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class AttendanceDAOImpl implements AttendanceDAO{

    private final AttendanceRepository attendanceRepository;


    @Override
    public BigDecimal getTotalOvertimeHoursByEmployeeAndDateRange(String employeeId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.getTotalOvertimeHoursByEmployeeAndDateRange(employeeId, startDate, endDate);
    }

    @Override
    public AttendanceEntity findById(Long id) {
        return attendanceRepository.findById(id).orElse(null);
    }

    @Override
    public AttendanceEntity save(AttendanceEntity attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public List<AttendanceEntity> findByEmployeeId(String employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId);
    }

    @Override
    public Optional<AttendanceEntity> findIdByEmployeeId(String employeeId) {
        return Optional.ofNullable(attendanceRepository.findIdByEmployeeId(employeeId));
    }
/**
    @Override // 외근 추가
    public AttendanceEntity findWorkingOutside() {
        return attendanceRepository.findWorkingOutside();
    }
    */
}

