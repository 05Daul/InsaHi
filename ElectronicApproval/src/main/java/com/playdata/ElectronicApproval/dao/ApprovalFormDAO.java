package com.playdata.ElectronicApproval.dao;

import com.playdata.ElectronicApproval.entity.ApprovalFormEntity;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface ApprovalFormDAO {

  ApprovalFormEntity save(ApprovalFormEntity ApprovalFormEntity);

  List<ApprovalFormEntity> saveAll(List<ApprovalFormEntity> ApprovalLineEntities);

  List<ApprovalFormEntity> findAll();

  List<ApprovalFormEntity> findAllByEmployeeId(String employeeId);

  List<ApprovalFormEntity> findAllByEmployeeIdAndApprovalStatus(String employeeId,
      ApprovalStatus approvalStatus);

  List<ApprovalFormEntity> findAllByApprovalStatus(ApprovalStatus approvalStatus);


  ApprovalFormEntity findById(String id);

  List<ApprovalFormEntity> findAll(Sort sort);

  List<ApprovalFormEntity> findAll(Sort sort, Pageable pageable);


}
