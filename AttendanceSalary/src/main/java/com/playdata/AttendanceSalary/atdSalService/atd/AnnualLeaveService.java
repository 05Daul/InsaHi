package com.playdata.AttendanceSalary.atdSalService.atd;

import com.playdata.AttendanceSalary.atdSalDto.atd.AnnualLeaveRequestDTO;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import java.util.List;

public interface AnnualLeaveService {

  void submit(String employeeId, String companyCode, AnnualLeaveRequestDTO dto);

  List<AnnualLeaveUsageEntity> findAllByEmployeeId(String employeeId);

  List<AnnualLeaveUsageEntity> findAllByCompanyCode(String companyCode, String status);

  void approveLeave(AnnualLeaveRequestDTO requestDTO);

  void rejectLeave(AnnualLeaveRequestDTO requestDTO);

  void approveAdditionalLeave(AnnualLeaveRequestDTO requestDTO);

  void executeLeaveGrant();
}
