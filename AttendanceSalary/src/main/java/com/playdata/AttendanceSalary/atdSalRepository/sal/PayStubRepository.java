package com.playdata.AttendanceSalary.atdSalRepository.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.PayStubEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PayStubRepository extends JpaRepository<PayStubEntity,Long> {
    PayStubEntity findByEmployeeId(@Param("employeeId") String employeeId);
    /// sort 추ㄷ
}
