package com.playdata.ElectronicApproval.dto;

import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import com.playdata.ElectronicApproval.entity.DeleteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalFileDTO {

  private String id;
  private String name;
  private String text;
  private String approvalForm;
  private ApprovalStatus status;
  private DeleteStatus deleteStatus;//delete_status
  private boolean deleted;
  private String companyCode;
  private String employeeId;


}

