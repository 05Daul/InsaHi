package com.playdata.ElectronicApproval.common.publicEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "approval_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String approvalFileId;
  private String originalFilename;
  private String storeFilename;
}
