package com.playdata.AttendanceSalary.atdSalRepository.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {
    @Query("SELECT COALESCE(SUM(a.overtimeHours), 0) " +
            "FROM AttendanceEntity a " +
            "WHERE a.employeeId = :employeeId " +
            "AND a.workDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalOvertimeHoursByEmployeeAndDateRange(
            @Param("employeeId") String employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<AttendanceEntity> findByEmployeeId(@Param("employeeId") String employeeId);

    AttendanceEntity findIdByEmployeeId(@Param("employeeId") String employeeId);

}

