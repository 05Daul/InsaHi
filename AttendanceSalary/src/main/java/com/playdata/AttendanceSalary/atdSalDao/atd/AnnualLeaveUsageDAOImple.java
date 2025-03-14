package com.playdata.AttendanceSalary.atdSalDao.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import com.playdata.AttendanceSalary.atdSalEntity.atd.LeaveApprovalStatus;
import com.playdata.AttendanceSalary.atdSalRepository.atd.AnnualLeaveUsageRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnnualLeaveUsageDAOImple implements AnnualLeaveUsageDAO {

  private final AnnualLeaveUsageRepository annualLeaveUsageRepository;

  @Override
  public List<AnnualLeaveUsageEntity> findAllByCompanyCodeAndLeaveApprovalStatus(String companyCode,
      LeaveApprovalStatus approvalStatus) {
    return annualLeaveUsageRepository.findAllByCompanyCodeAndLeaveApprovalStatus(companyCode,
        approvalStatus);
  }

  @Override
  public Optional<AnnualLeaveUsageEntity> findById(long id) {
    return annualLeaveUsageRepository.findById(id);
  }

  @Override
  public void save(AnnualLeaveUsageEntity usage) {
    annualLeaveUsageRepository.save(usage);
  }

  @Override
  public List<AnnualLeaveUsageEntity> findByEmployeeId(String employeeId) {
    return annualLeaveUsageRepository.findByEmployeeId(employeeId);
  }
}
