package com.playdata.ElectronicApproval.dao;

import com.playdata.ElectronicApproval.common.publicEntity.FileEntity;
import com.playdata.ElectronicApproval.dto.FileDTO;
import java.util.List;
import java.util.Optional;

public interface FileDAO {

  List<FileEntity> findAllByApprovalFileId(String approvalId);

  void saveAll(List<FileDTO> uploadedFiles);

  Optional<FileEntity> findById(Long fileId);
}
