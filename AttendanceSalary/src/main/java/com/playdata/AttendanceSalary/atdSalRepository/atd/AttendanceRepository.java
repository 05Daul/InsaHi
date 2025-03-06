package com.playdata.attendanceSalary.atdSalRepository.atd;

import com.playdata.attendanceSalary.atdSalEntity.atd.AttendanceEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long> {
    // @Query("SELECT a FROM AttendanceEntity a WHERE Employee.employeeId= :employeeId AND a.companyCode = :companyCode")
   // AttendanceEntity findByIdCode(@Param("employeeId") String employeeId, @Param("companyCode") String companyCode);
}

