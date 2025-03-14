package com.playdata.ElectronicApproval.dao;

import com.playdata.ElectronicApproval.entity.ApprovalLineDetailEntity;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import com.playdata.ElectronicApproval.repository.ApprovalLineDetailRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApprovalLineDetailDAOImpl implements ApprovalLineDetailDAO {

  private final ApprovalLineDetailRepository approvalLineDetailRepository;

  @Override
  public ApprovalLineDetailEntity save(ApprovalLineDetailEntity ApprovalLineDetailEntity) {
    return approvalLineDetailRepository.save(ApprovalLineDetailEntity);

  }

  @Override
  public List<ApprovalLineDetailEntity> saveAll(
      List<ApprovalLineDetailEntity> ApprovalLineEntities) {
    return approvalLineDetailRepository.saveAll(ApprovalLineEntities);
  }

  @Override
  public List<ApprovalLineDetailEntity> findAll() {
    return List.of();
  }

//  @Override
//  public List<ApprovalLineDetailEntity> findAllByEmployeeId(String employeeId) {
//    return approvalLineDetailRepository.;
//  }

  @Override
  public List<ApprovalLineDetailEntity> findAllByEmployeeIdAndApprovalStatus(String employeeId,
      ApprovalStatus approvalStatus) {
    return List.of();
  }

  @Override
  public List<ApprovalLineDetailEntity> findAllByApprovalStatus(ApprovalStatus approvalStatus) {
    return List.of();
  }

  @Override
  public void update(ApprovalLineDetailEntity ApprovalLineDetailEntity) {
    approvalLineDetailRepository.save(ApprovalLineDetailEntity);
  }

  @Override
  public ApprovalLineDetailEntity findById(String id) {
    return null;
  }

  @Override
  public List<ApprovalLineDetailEntity> findAll(Sort sort) {
    return List.of();
  }

  @Override
  public List<ApprovalLineDetailEntity> findAll(Sort sort, Pageable pageable) {
    return List.of();
  }

  //  @Override
  public void updateDetailStatus(ApprovalLineDetailEntity lineDetail, ApprovalStatus approvalStatus,
      String reason) {
    lineDetail.setStatus(approvalStatus);
    lineDetail.setReason(reason);
    approvalLineDetailRepository.save(lineDetail);
  }
}
