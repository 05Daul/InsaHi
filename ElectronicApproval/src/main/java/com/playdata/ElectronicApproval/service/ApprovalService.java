package com.playdata.ElectronicApproval.service;

import com.playdata.ElectronicApproval.dto.ApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.FileDTO;
import com.playdata.ElectronicApproval.dto.RequestApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.ResponseApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.SubmitApprovalRequest;
import java.util.List;

public interface ApprovalService {

//  void submitApproval(SubmitApprovalRequest request);

  void submitApproval(SubmitApprovalRequest request, List<FileDTO> uploadedFiles);

  void approveUpdateStatus(String approvalLineId, String approveOrNot, String reason);

  List<ApprovalFileDTO>
  getApprovalFiles(String employeeId, int menu);

  ResponseApprovalFileDTO getApprovalFile(String approvalFileId);
}
