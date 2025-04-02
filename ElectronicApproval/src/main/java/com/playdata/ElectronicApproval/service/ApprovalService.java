package com.playdata.ElectronicApproval.service;

import com.playdata.ElectronicApproval.dto.ApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.FileDTO;
import com.playdata.ElectronicApproval.dto.RequestApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.ResponseApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.SubmitApprovalRequest;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApprovalService {

//  void submitApproval(SubmitApprovalRequest request);

  void submitApproval(SubmitApprovalRequest request, List<FileDTO> uploadedFiles);

  boolean hasFirstPending(String employeeId);

  void approveUpdateStatus(String approvalLineId, ApprovalStatus approveOrNot, String reason);

  Page<ApprovalFileDTO> getApprovalFiles(String employeeId, int menu, Pageable pageable);

  ResponseApprovalFileDTO getApprovalFile(String approvalFileId);
}
