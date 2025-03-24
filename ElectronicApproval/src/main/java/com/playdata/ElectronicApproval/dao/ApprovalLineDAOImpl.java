package com.playdata.ElectronicApproval.dao;

import com.playdata.ElectronicApproval.entity.ApprovalFileEntity;
import com.playdata.ElectronicApproval.entity.ApprovalLineEntity;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import com.playdata.ElectronicApproval.repository.ApprovalLineRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
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
  /**/ public List<ApprovalLineEntity> findAllByEmployeeIdAndApprovalOrder(String employeeId,
      int approvalOrder) {
    return approvalLineRepository.findAllByEmployeeIdAndApprovalOrder(employeeId, approvalOrder);
  }

  /**/
  @Override
  public List<ApprovalLineEntity> findAllByEmployeeIdAndApprovalOrderNotZero(String employeeId) {
    return approvalLineRepository.findAllByEmployeeIdAndApprovalOrderNotZero(employeeId);
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
  public List<ApprovalLineEntity> findAllByApprovalFile(ApprovalFileEntity approvalFile) {
    return approvalLineRepository.findAllByApprovalFile(approvalFile);
  }

  @Override
  public Optional<ApprovalLineEntity> findById(String id) {
    log.info("  public Optional<ApprovalLineEntity> findById(String id) ::: {}", id);
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
