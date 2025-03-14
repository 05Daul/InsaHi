package com.playdata.ElectronicApproval.service;

import com.playdata.ElectronicApproval.dao.ApprovalFileDAO;
import com.playdata.ElectronicApproval.dao.ApprovalLineDAO;
import com.playdata.ElectronicApproval.dao.ApprovalLineDetailDAO;
import com.playdata.ElectronicApproval.dto.ApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.RequestApprovalFileDTO;
import com.playdata.ElectronicApproval.entity.ApprovalFileEntity;
import com.playdata.ElectronicApproval.entity.ApprovalLineEntity;
import com.playdata.ElectronicApproval.entity.ApprovalLineDetailEntity;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import com.playdata.ElectronicApproval.repository.ApprovalFormRepository;
import jakarta.transaction.Transactional;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional
public class ApprovalServiceImpl implements ApprovalService {

  private final ApprovalFileDAO approvalFileDao;
  private final ApprovalLineDAO approvalLineDao;
  private final ApprovalLineDetailDAO approvalLineDetailDAO;
  private final ModelMapper modelMapper;

  // 결재 요청 제출 (나름 리팩토링된 메서드)
  public void submitApproval(String employeeId, String companyCode, RequestApprovalFileDTO dto,
      List<String> approvers, List<String> referrers) {
    log.info("Submitting Approval for DTO: {}", dto);

    // 1. 문서 생성
    ApprovalFileEntity approvalFileEntity = modelMapper.map(dto, ApprovalFileEntity.class);
    approvalFileEntity.setId(dto.getApprovalForm());
    ApprovalFileEntity savedEntity = approvalFileDao.save(approvalFileEntity);
    log.info("Approval File Created: {}", savedEntity.getId());

    //  2. 결재 라인 및 참조인 생성
    List<ApprovalLineEntity> approverLines = createApprovalLines(savedEntity, approvers,
        companyCode, true);
    List<ApprovalLineEntity> referrerLines = createApprovalLines(savedEntity, referrers,
        companyCode, false);

    //  3. 결재 라인 저장
    List<ApprovalLineEntity> savedLines = approvalLineDao.saveAll(approverLines);
    approvalLineDao.saveAll(referrerLines);
    log.info("Approval Lines Saved: {}", savedLines);

    // 4. 첫 번째 결재 라인에 전체 라인 정보 설정
    if (!savedLines.isEmpty()) {
      savedLines.get(0).getApprovalFile().setApprovalLineEntities(savedLines);
    }
  }

  //  결재자 및 참조인 라인을 생성하는 메서드
  private List<ApprovalLineEntity> createApprovalLines(ApprovalFileEntity approvalFile,
      List<String> employees, String companyId, boolean isApprover) {
    return IntStream.range(0, employees.size())
        .mapToObj(i -> createApprovalLine(approvalFile, employees.get(i), isApprover ? i + 1 : 0,
            companyId))
        .collect(Collectors.toList());
  }

  //  ApprovalLineEntity 생성 메서드 (결재자 및 참조인 겸용)
  private ApprovalLineEntity createApprovalLine(ApprovalFileEntity approvalFile, String employeeId,
      int order, String companyId) {
    ApprovalStatus approvalStatus =
        (order == 0) ? ApprovalStatus.REFERENCES : ApprovalStatus.PENDING;

    ApprovalLineDetailEntity approvalLineDetail = new ApprovalLineDetailEntity(
        null, approvalStatus, null, companyId);

    return new ApprovalLineEntity(
        null, approvalFile, approvalLineDetail, employeeId, order, companyId);
  }

  // 결재 승인/반려 처리
  public void approveUpdateStatus(String approvalLineId, String approveOrNot, String reason) {
    // 결제라인 조회
    ApprovalLineEntity lineEntity = approvalLineDao.findById(approvalLineId)
        .orElseThrow(() -> new IllegalArgumentException("Approval line not found"));
    ApprovalStatus approvalStatus = ApprovalStatus.valueOf(approveOrNot);
    List<ApprovalLineEntity> lines = lineEntity.getApprovalFile().getApprovalLineEntities();

    if ((approvalStatus == ApprovalStatus.REJECTED) || (approvalStatus == ApprovalStatus.APPROVED
        && lineEntity.equals(lines.get(lines.size() - 1)))) {
      lineEntity.getApprovalFile().setStatus(approvalStatus);
//      updateFileStatus(lineEntity, approvalStatus);
      approvalFileDao.save(lineEntity.getApprovalFile());
    }
//    updateDetailStatus(lineEntity, approvalStatus, reason);
    lineEntity.getApprovalLineDetail().setStatus(approvalStatus);
    lineEntity.getApprovalLineDetail().setReason(reason);
    approvalLineDetailDAO.save(lineEntity.getApprovalLineDetail());

  }


  // 결재 문서 조회
  public List<ApprovalFileDTO> getApprovalFiles(String employeeId, int menu) {
    List<ApprovalFileEntity> files = new ArrayList<>();
    if (menu == 1) {
      files = approvalFileDao.findAllByEmployeeId(employeeId);
/*
      log.info("본인이 상신한 문서 : {}, 본인 :{}, lineId1 : {}", ownedFiles.get(0).getId(),
          ownedFiles.get(0).getEmployeeId(),
          ownedFiles.get(0).getApprovalLineEntities().get(0).getId());
*/
    } else if (menu == 2) {
      // 결재 지정된 문서 조회 -> file에 저장된 line을 가져와서 employee_id 조회
      files = findByAssignedApproval(employeeId);
/*
      findByAssignedApproval(employeeId).stream()
          .map(file -> {
            log.info("fileID: {}", file.getId());
            return file.getId();
          }).collect(Collectors.toList());
      log.info("결재 지정된 문서 : {}", assignedFiles.get(0).getId());
*/
    } else if (menu == 3) {

      // 결재할 문서 조회 -> file에 저장된 line을 가져와서 employee_id 조회 Pending
/*
      List<ApprovalFileEntity> pendingFiles = approvalFileDao.findAllByEmployeeIdAndApprovalStatus(
          employeeId, ApprovalStatus.PENDING);
      approvalFileDao.findAllByEmployeeIdAndApprovalStatus(
          employeeId, ApprovalStatus.PENDING).stream().map(approvalFileEntity -> {
        log.info("findAllByEmployeeIdAndApprovalStatus : {}", approvalFileEntity);
        return approvalFileEntity;
      }).collect(Collectors.toList());
*/
//    log.info("결재할 문서 조회 : {}", pendingFiles.get(0).getId());
      files = firstpendingFiles(employeeId);
      firstpendingFiles(employeeId).stream()
          .map(file -> {
            log.info("findAllByEmployeeIdAndApprovalStatus : {}", file.getId());
            return file;
          }).collect(Collectors.toList());
//      log.info("결재할 문서 조회2 : {}", pendingApprovalFiles.get(0).getId());

    }
    List<ApprovalFileDTO> approvalFileDTOS = files.stream().map(file -> convertToDto(file))
        .collect(Collectors.toList());
    return approvalFileDTOS;

    // 참조인으로 지정된 문서 조회 -> 파일 전체에서 참조인 컬럼을

//    approvalDao.findById(id)
//        .map(entity -> {
//          log.info("Approval Document: {}", entity);
//          return modelMapper.map(entity, ApprovalFileDTO.class);
//        })
//        .orElseThrow(() -> new IllegalArgumentException("Document not found"));

//    return null;
  }

  @Override
  public ApprovalFileDTO getApprovalFile(String approvalFileId) {
    return convertToDto(approvalFileDao.findById(approvalFileId)
        .orElseThrow(() -> new IllegalArgumentException("Approval file not found")));
  }

  private ApprovalFileDTO convertToDto(ApprovalFileEntity approvalFileEntity) {
    return modelMapper.map(approvalFileEntity, ApprovalFileDTO.class);
  }

//  private List<ApprovalFileEntity> findByEmployeeId(String employeeId) {
//    return approvalFileDao.findFileByEmployeeId(employeeId);
//  }
//
//  private List<ApprovalFileEntity> findByPendingApproval(String employeeId) {
//    ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
//    List<ApprovalFileEntity> files = approvalFileDao.findAllApprovalLineDetailsByApprovalLineAndApprovalStatus(
//            line, approvalStatus)
//        .stream()
//        .filter((allfiles) -> isPendingTurnApproval(allfiles))
//        .collect(Collectors.toList());
//    return files;
//  }
//
//  private boolean isPendingTurnApproval(ApprovalFileEntity allfiles) {
//    return allfiles.getApprovalLineEntities().stream()
//        .anyMatch(line -> approvalFileDao.findAllApprovalLineDetailsByApprovalLineAndApprovalStatus(
//            line, ).stream()
//            .
//        )
//  }

  private List<ApprovalFileEntity> findByAssignedApproval(String employeeId) {
//    approvalLineDao.findAllByEmployeeIdAndApprovalStatus(EmployeeId, ApprovalStatus.PENDING);
    return approvalLineDao.findAllByEmployeeId(employeeId).stream()
        .map(line -> line.getApprovalFile()).collect(Collectors.toList());
//    return null;
  }

  private List<ApprovalFileEntity> pendingFiles(String employeeId) {
//    approvalLineDao.findAllByEmployeeIdAndApprovalStatus(EmployeeId, ApprovalStatus.PENDING);
    return approvalLineDao.findAllByEmployeeIdAndApprovalStatus(employeeId, ApprovalStatus.PENDING)
        .stream()
        .map(line -> line.getApprovalFile()).collect(Collectors.toList());
//    return null;
  }

  private List<ApprovalFileEntity> firstpendingFiles(String employeeId) {
//    approvalLineDao.findAllByEmployeeIdAndApprovalStatus(EmployeeId, ApprovalStatus.PENDING);
    return approvalLineDao.findAllByEmployeeIdAndFirstPending(employeeId) // find
        .stream()
        .map(line -> line.getApprovalFile()).collect(Collectors.toList());
  }

}
