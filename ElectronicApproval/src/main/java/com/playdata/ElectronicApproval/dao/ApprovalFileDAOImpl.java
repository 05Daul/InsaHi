package com.playdata.ElectronicApproval.dao;

import com.playdata.ElectronicApproval.entity.ApprovalFileEntity;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import com.playdata.ElectronicApproval.repository.ApprovalFileRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ApprovalFileDAOImpl implements ApprovalFileDAO {

  private final ApprovalFileRepository approvalFileRepository;


  @Override
  public ApprovalFileEntity save(ApprovalFileEntity approvalFileEntity) {
    return approvalFileRepository.save(approvalFileEntity);
  }

  @Override
  public List<ApprovalFileEntity> saveAll(List<ApprovalFileEntity> approvalFileEntities) {
    return approvalFileRepository.saveAll(approvalFileEntities);
  }

//  @Override
//  public void updateFileStatus(ApprovalFileEntity approvalFileEntity,
//      ApprovalStatus approvalStatus) {
//    approvalFileEntity.setStatus(approvalStatus);
//    approvalFileRepository.save(approvalFileEntity);
//  }

  @Override
  public List<ApprovalFileEntity> findAll() {
    return approvalFileRepository.findAll();
  }

  @Override
  public List<ApprovalFileEntity> findAllByEmployeeId(String employeeId) {
    return approvalFileRepository.findAllByEmployeeId(employeeId);
  }

  @Override
  public List<ApprovalFileEntity> findAllByEmployeeIdAndApprovalStatus(String employeeId,
      ApprovalStatus approvalStatus) {
    return List.of();
  }

  @Override
  public List<ApprovalFileEntity> findAllByApprovalStatus(ApprovalStatus approvalStatus) {
    return List.of();
  }

  @Override
  public Optional<ApprovalFileEntity> findById(String id) {
    return approvalFileRepository.findById(id);
  }

  @Override
  public List<ApprovalFileEntity> findAll(Sort sort) {
    return List.of();
  }

  @Override
  public List<ApprovalFileEntity> findAll(Sort sort, Pageable pageable) {
    return List.of();
  }
}
