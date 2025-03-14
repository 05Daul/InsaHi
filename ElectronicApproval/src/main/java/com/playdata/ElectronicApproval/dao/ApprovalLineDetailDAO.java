package com.playdata.ElectronicApproval.dao;

import com.playdata.ElectronicApproval.entity.ApprovalLineDetailEntity;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ApprovalLineDetailDAO {

  ApprovalLineDetailEntity save(ApprovalLineDetailEntity ApprovalLineDetailEntity);

  List<ApprovalLineDetailEntity> saveAll(List<ApprovalLineDetailEntity> ApprovalLineEntities);

  List<ApprovalLineDetailEntity> findAll();

//  List<ApprovalLineDetailEntity> findAllByEmployeeId(String employeeId);

  List<ApprovalLineDetailEntity> findAllByEmployeeIdAndApprovalStatus(String employeeId,
      ApprovalStatus approvalStatus);

  List<ApprovalLineDetailEntity> findAllByApprovalStatus(ApprovalStatus approvalStatus);

  void update(ApprovalLineDetailEntity ApprovalLineDetailEntity);
//  void update(String)


  ApprovalLineDetailEntity findById(String id);

  List<ApprovalLineDetailEntity> findAll(Sort sort);

  List<ApprovalLineDetailEntity> findAll(Sort sort, Pageable pageable);

//  void updateDetailStatus(ApprovalLineDetailEntity lineDetail, ApprovalStatus approvalStatus,
//      String reason);
}
