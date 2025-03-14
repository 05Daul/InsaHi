package com.playdata.ElectronicApproval.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestApprovalFileDTO {

  private String id;
  private String name;
  private String text;
  private String approvalForm;
  private String companyCode;
  private String employeeId;
}
