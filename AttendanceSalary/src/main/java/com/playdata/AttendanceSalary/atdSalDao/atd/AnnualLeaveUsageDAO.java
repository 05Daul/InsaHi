package com.playdata.AttendanceSalary.atdSalDao.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import com.playdata.AttendanceSalary.atdSalEntity.atd.LeaveApprovalStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnualLeaveUsageDAO {

  List<AnnualLeaveUsageEntity> findAllByCompanyCodeAndLeaveApprovalStatus(String companyCode,
      LeaveApprovalStatus approvalStatus);

  Optional<AnnualLeaveUsageEntity> findById(long id);

  void save(AnnualLeaveUsageEntity leave);

  List<AnnualLeaveUsageEntity> findByEmployeeId(String employeeId);

  boolean existsOverlappingLeave(String employeeId, LocalDate startDate, LocalDate stopDate);

  List<AnnualLeaveUsageEntity> findByEmployeeIdAndLeaveApprovalStatus(String employeeId,
      LeaveApprovalStatus leaveApprovalStatus);

  Page<AnnualLeaveUsageEntity> findAllByCompanyCodeAndLeaveApprovalStatus(String companyCode,
      LeaveApprovalStatus status, Pageable pageable);

  Page<AnnualLeaveUsageEntity> findAllByCompanyCode(String companyCode, Pageable pageable);

  Page<AnnualLeaveUsageEntity> findAllByEmployeeId(String employeeId, Pageable pageable);

  Page<AnnualLeaveUsageEntity> findAllByEmployeeIdAndLeaveApprovalStatus(String employeeId,
      LeaveApprovalStatus leaveApprovalStatus, Pageable pageable);
}
