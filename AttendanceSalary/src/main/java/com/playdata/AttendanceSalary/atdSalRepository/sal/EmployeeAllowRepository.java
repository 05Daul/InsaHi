package com.playdata.AttendanceSalary.atdSalRepository.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.EmployeeAllowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeAllowRepository extends JpaRepository<EmployeeAllowEntity, Long> {
    List<EmployeeAllowEntity> findByEmployeeId(@Param("employeeId") String employeeId);

}
