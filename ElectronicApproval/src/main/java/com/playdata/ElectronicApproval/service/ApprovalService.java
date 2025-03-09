package com.playdata.ElectronicApproval.service;

import com.playdata.ElectronicApproval.dto.RequestApprovalFileDTO;
import java.util.List;

public interface ApprovalService {

  void submitApproval(String employeeId, String CompanyCode, RequestApprovalFileDTO dto,
      List<String> approvers, List<String> referrers);

  void approveUpdateStatus(String approvalLineId, String approveOrNot, String reason);

}
