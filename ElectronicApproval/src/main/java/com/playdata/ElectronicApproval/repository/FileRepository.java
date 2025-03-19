package com.playdata.ElectronicApproval.repository;

import com.playdata.ElectronicApproval.common.publicEntity.FileEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

  List<FileEntity> findAllByApprovalFileId(String approvalFileId);
}
