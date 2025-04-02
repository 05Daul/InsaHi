package com.playdata.AttendanceSalary.atdSalDao.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import com.playdata.AttendanceSalary.atdSalEntity.atd.LeaveApprovalStatus;
import com.playdata.AttendanceSalary.atdSalRepository.atd.AnnualLeaveUsageRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
  public List<AnnualLeaveUsageEntity> findByEmployeeIdAndLeaveApprovalStatus(String employeeId,
      LeaveApprovalStatus leaveApprovalStatus) {
    return annualLeaveUsageRepository.findByEmployeeIdAndLeaveApprovalStatus(employeeId,
        leaveApprovalStatus);
  }

  @Override
  public Page<AnnualLeaveUsageEntity> findAllByCompanyCodeAndLeaveApprovalStatus(String companyCode,
      LeaveApprovalStatus status, Pageable pageable) {
    return annualLeaveUsageRepository.findAllByCompanyCodeAndLeaveApprovalStatus(companyCode,
        status, pageable);
  }

  @Override
  public Page<AnnualLeaveUsageEntity> findAllByCompanyCode(String companyCode, Pageable pageable) {
    return annualLeaveUsageRepository.findAllByCompanyCode(companyCode, pageable);
  }

  @Override
  public Page<AnnualLeaveUsageEntity> findAllByEmployeeId(String employeeId, Pageable pageable) {
    return annualLeaveUsageRepository.findAllByEmployeeId(employeeId, pageable);
  }

  @Override
  public Page<AnnualLeaveUsageEntity> findAllByEmployeeIdAndLeaveApprovalStatus(String employeeId,
      LeaveApprovalStatus leaveApprovalStatus, Pageable pageable) {
    return annualLeaveUsageRepository.findAllByEmployeeIdAndLeaveApprovalStatus(employeeId,
        leaveApprovalStatus, pageable);
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

  @Override
  public boolean existsOverlappingLeave(String employeeId, LocalDate startDate,
      LocalDate stopDate) {
    return annualLeaveUsageRepository.existsByEmployeeIdAndLeaveApprovalStatusInAndDateOverlap(
        employeeId,
        List.of(LeaveApprovalStatus.PENDING, LeaveApprovalStatus.APPROVED),
        startDate,
        stopDate
    );
  }


}
