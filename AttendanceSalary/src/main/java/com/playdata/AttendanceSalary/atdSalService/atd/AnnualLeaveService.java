package com.playdata.AttendanceSalary.atdSalService.atd;

import com.playdata.AttendanceSalary.atdSalDto.atd.AnnualLeaveDTO;
import com.playdata.AttendanceSalary.atdSalDto.atd.AnnualLeaveRequestDTO;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveEntity;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import java.util.List;
import org.springframework.data.domain.Page;

public interface AnnualLeaveService {

  AnnualLeaveRequestDTO findById(Long id);

  void submit(String employeeId, String companyCode, AnnualLeaveRequestDTO dto);

  //  List<AnnualLeaveUsageEntity> findAllByEmployeeId(String employeeId);
  List<AnnualLeaveRequestDTO> findAllByEmployeeIdAndLeaveApprovalStatus(String employeeId,
      String status);

  //  List<AnnualLeaveUsageEntity> findAllByCompanyCode(String companyCode, String status);
  List<AnnualLeaveRequestDTO> findAllByCompanyCodeAndLeaveApprovalStatus(String companyCode,
      String status);

  void approveLeave(AnnualLeaveRequestDTO requestDTO);

  void rejectLeave(AnnualLeaveRequestDTO requestDTO);

  void approveAdditionalLeave(AnnualLeaveRequestDTO requestDTO);

  void executeLeaveGrant();

  AnnualLeaveDTO findLatestAnnualLeave(String employeeId);

  Page<AnnualLeaveRequestDTO> findAllByCompanyCodeAndLeaveApprovalStatusWithPagination(
      String companyCode, String status, int page, int size, String sort);

  Page<AnnualLeaveRequestDTO> findAllByEmployeeIdAndLeaveApprovalStatusWithPagination(
      String employeeId, String status, int page, int size, String sort);
}
