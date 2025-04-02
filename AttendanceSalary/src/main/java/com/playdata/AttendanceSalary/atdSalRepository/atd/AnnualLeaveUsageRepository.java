package com.playdata.AttendanceSalary.atdSalRepository.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import com.playdata.AttendanceSalary.atdSalEntity.atd.LeaveApprovalStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnnualLeaveUsageRepository extends JpaRepository<AnnualLeaveUsageEntity, Long> {

  Optional<AnnualLeaveUsageEntity> findById(Long id);

  List<AnnualLeaveUsageEntity> findAllByCompanyCodeAndLeaveApprovalStatus(String companyCode,
      LeaveApprovalStatus approvalStatus);

  List<AnnualLeaveUsageEntity> findByEmployeeIdAndLeaveApprovalStatus(String employeeId,
      LeaveApprovalStatus leaveApprovalStatus);

  List<AnnualLeaveUsageEntity> findByEmployeeId(String employeeId);

  @Query(
      "SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " + "FROM AnnualLeaveUsageEntity a "
          + "WHERE a.employeeId = :employeeId " + "AND a.leaveApprovalStatus IN :statuses "
          + "AND (a.startDate <= :stopDate AND a.stopDate >= :startDate)")
  boolean existsByEmployeeIdAndLeaveApprovalStatusInAndDateOverlap(
      @Param("employeeId") String employeeId, @Param("statuses") List<LeaveApprovalStatus> statuses,
      @Param("startDate") LocalDate startDate, @Param("stopDate") LocalDate stopDate);

  Page<AnnualLeaveUsageEntity> findAllByCompanyCodeAndLeaveApprovalStatus(String companyCode,
      LeaveApprovalStatus status, Pageable pageable);

  Page<AnnualLeaveUsageEntity> findAllByCompanyCode(String companyCode, Pageable pageable);

  Page<AnnualLeaveUsageEntity> findAllByEmployeeId(String employeeId, Pageable pageable);

  Page<AnnualLeaveUsageEntity> findAllByEmployeeIdAndLeaveApprovalStatus(String employeeId,
      LeaveApprovalStatus leaveApprovalStatus, Pageable pageable);
}
