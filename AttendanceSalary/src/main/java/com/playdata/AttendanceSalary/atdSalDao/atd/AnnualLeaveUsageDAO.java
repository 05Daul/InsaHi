package com.playdata.AttendanceSalary.atdSalDao.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import com.playdata.AttendanceSalary.atdSalEntity.atd.LeaveApprovalStatus;
import java.util.List;
import java.util.Optional;

public interface AnnualLeaveUsageDAO {

  List<AnnualLeaveUsageEntity> findAllByCompanyCodeAndLeaveApprovalStatus(String companyCode,
      LeaveApprovalStatus approvalStatus);

  Optional<AnnualLeaveUsageEntity> findById(long id);

  void save(AnnualLeaveUsageEntity leave);

  List<AnnualLeaveUsageEntity> findByEmployeeId(String employeeId);
}
