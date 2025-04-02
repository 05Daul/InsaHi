package com.playdata.AttendanceSalary.atdSalRepository.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnualLeaveRepository extends JpaRepository<AnnualLeaveEntity, Long> {

  Optional<AnnualLeaveEntity> findTopByEmployeeIdOrderByCreateDateDesc(String employeeId);

}
