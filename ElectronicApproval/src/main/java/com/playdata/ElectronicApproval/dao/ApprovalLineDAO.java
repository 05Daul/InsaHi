package com.playdata.ElectronicApproval.dao;

import com.playdata.ElectronicApproval.entity.ApprovalFileEntity;
import com.playdata.ElectronicApproval.entity.ApprovalLineEntity;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ApprovalLineDAO {

  ApprovalLineEntity save(ApprovalLineEntity ApprovalLineEntity);

  List<ApprovalLineEntity> saveAll(List<ApprovalLineEntity> ApprovalLineEntities);

  List<ApprovalLineEntity> findAll();

  /**/List<ApprovalLineEntity> findAllByEmployeeIdAndApprovalOrder(String employeeId,
      int approvalOrder);

  List<ApprovalLineEntity> findAllByEmployeeIdAndApprovalOrderNotZero(String employeeId);

  List<ApprovalLineEntity> findAllByEmployeeIdAndApprovalStatus(String employeeId,
      ApprovalStatus approvalStatus);

  List<ApprovalLineEntity> findAllByApprovalStatus(ApprovalStatus approvalStatus);

  List<ApprovalLineEntity> findAllByApprovalFile(ApprovalFileEntity approvalFile);

  Optional<ApprovalLineEntity> findById(String id);

  List<ApprovalLineEntity> findAll(Sort sort);

  List<ApprovalLineEntity> findAll(Sort sort, Pageable pageable);


  List<ApprovalLineEntity> findAllByEmployeeIdAndFirstPending(String employeeId);
}
