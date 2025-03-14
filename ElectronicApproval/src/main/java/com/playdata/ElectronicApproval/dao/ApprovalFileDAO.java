package com.playdata.ElectronicApproval.dao;

import com.playdata.ElectronicApproval.entity.ApprovalFileEntity;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ApprovalFileDAO {

  ApprovalFileEntity save(ApprovalFileEntity approvalFileEntity);

  List<ApprovalFileEntity> saveAll(List<ApprovalFileEntity> approvalFileEntities);

//  void updateFileStatus(ApprovalFileEntity approvalFileEntity, ApprovalStatus approvalStatus);

  List<ApprovalFileEntity> findAll();

  List<ApprovalFileEntity> findAllByEmployeeId(String employeeId);

  List<ApprovalFileEntity> findAllByEmployeeIdAndApprovalStatus(String employeeId,
      ApprovalStatus approvalStatus);

  List<ApprovalFileEntity> findAllByApprovalStatus(ApprovalStatus approvalStatus);


  Optional<ApprovalFileEntity> findById(String id);

  List<ApprovalFileEntity> findAll(Sort sort);

  List<ApprovalFileEntity> findAll(Sort sort, Pageable pageable);

}
