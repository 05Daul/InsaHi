package com.playdata.ElectronicApproval.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitApprovalRequest {

  private String id;
  private String name;
  private String text;
  private String companyCode;
  private String employeeId;
  private List<String> approvers;
  private List<String> referencedIds;
//  private List<MultipartFile> files;

}
