package com.playdata.AttendanceSalary.atdSalDao.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveEntity;
import java.util.Optional;

public interface AnnualLeaveDAO {

  Optional<AnnualLeaveEntity> findById(Long annualLeaveId);

  void save(AnnualLeaveEntity leave);

  Optional<AnnualLeaveEntity> findLatestByEmployeeId(String employeeId);

}
