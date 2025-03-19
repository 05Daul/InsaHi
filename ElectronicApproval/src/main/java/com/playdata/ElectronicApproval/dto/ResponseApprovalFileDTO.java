package com.playdata.ElectronicApproval.dto;

import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import com.playdata.ElectronicApproval.entity.DeleteStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseApprovalFileDTO {

  private String id;
  private String name;
  private String text;
  private String status;
  private String deleteStatus;//delete_status
  private boolean deleted;
  private String companyCode;
  private String employeeId;
  private List<FileDTO> files;  // 해당 결재 문서에 첨부된 파일 목록

  public ResponseApprovalFileDTO(String s) {
    this.text = s;
    this.files = new ArrayList<>();
  }
}
