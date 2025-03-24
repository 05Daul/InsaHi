package com.playdata.ElectronicApproval.dto;

import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermitApprovalRequest {

  private String lineId;
  private ApprovalStatus approveOrNot;
  private String reason;
}
