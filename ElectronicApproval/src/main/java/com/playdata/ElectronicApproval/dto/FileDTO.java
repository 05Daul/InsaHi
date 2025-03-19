package com.playdata.ElectronicApproval.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {

  private Long approvalFileNo;     // PK 혹은 UUID
  private String approvalFileId;     // 결재 문서 ID (외래키)
  private String originalFilename;   // 원본 파일명
  private String storeFilename;      // 서버에 저장된 파일명
}