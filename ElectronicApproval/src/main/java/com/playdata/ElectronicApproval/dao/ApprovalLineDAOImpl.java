package com.playdata.ElectronicApproval.dao;

import com.playdata.ElectronicApproval.entity.ApprovalLineEntity;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import com.playdata.ElectronicApproval.repository.ApprovalLineRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApprovalLineDAOImpl implements ApprovalLineDAO {

  private final ApprovalLineRepository approvalLineRepository;


  @Override
  public ApprovalLineEntity save(ApprovalLineEntity ApprovalLineEntity) {
    return approvalLineRepository.save(ApprovalLineEntity);
  }

  @Override
  public List<ApprovalLineEntity> saveAll(List<ApprovalLineEntity> ApprovalLineEntities) {
    return approvalLineRepository.saveAll(ApprovalLineEntities);
  }

  @Override
  public List<ApprovalLineEntity> findAll() {
    return List.of();
  }

  @Override
  public List<ApprovalLineEntity> findAllByEmployeeId(String employeeId) {
    return approvalLineRepository.findAllByEmployeeId(employeeId);
  }

  @Override
  public List<ApprovalLineEntity> findAllByEmployeeIdAndApprovalStatus(String employeeId,
      ApprovalStatus approvalStatus) {
    return approvalLineRepository.findAllByEmployeeIdAndApprovalLineDetailStatus(employeeId,
        approvalStatus);
  }

  @Override
  public List<ApprovalLineEntity> findAllByApprovalStatus(ApprovalStatus approvalStatus) {
    return List.of();
  }

  @Override
  public Optional<ApprovalLineEntity> findById(String id) {
    return approvalLineRepository.findById(id);
  }

  @Override
  public List<ApprovalLineEntity> findAll(Sort sort) {
    return List.of();
  }

  @Override
  public List<ApprovalLineEntity> findAll(Sort sort, Pageable pageable) {
    return List.of();
  }

  @Override
  public List<ApprovalLineEntity> findAllByEmployeeIdAndFirstPending(String employeeId) {
    return approvalLineRepository.findFirstPendingLinesByEmployee(employeeId);
  }
}
