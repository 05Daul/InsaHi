package com.playdata.AttendanceSalary.atdSalRepository.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnualLeaveUsageRepository extends JpaRepository<AnnualLeaveUsageEntity,Long> {
}
