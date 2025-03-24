package com.playdata.ElectronicApproval.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalLineDTO {

  private String id;            // 결재 라인 ID
  private int approvalOrder;    // 결재 순서
  private String employeeId;    // 결재자 ID
  private String approvalStatus; // 결재 상태 추가

}
