

package com.playdata.ElectronicApproval.service;

import com.playdata.ElectronicApproval.dao.ApprovalFileDAO;
import com.playdata.ElectronicApproval.dao.ApprovalLineDAO;
import com.playdata.ElectronicApproval.dao.ApprovalLineDetailDAO;
import com.playdata.ElectronicApproval.dao.FileDAO;
import com.playdata.ElectronicApproval.dto.ApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.FileDTO;
import com.playdata.ElectronicApproval.dto.RequestApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.ResponseApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.SubmitApprovalRequest;
import com.playdata.ElectronicApproval.entity.ApprovalFileEntity;
import com.playdata.ElectronicApproval.entity.ApprovalLineEntity;
import com.playdata.ElectronicApproval.entity.ApprovalLineDetailEntity;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import com.playdata.ElectronicApproval.exception.ApprovalAlreadyProcessedException;
import com.playdata.ElectronicApproval.exception.ApprovalLineNotFoundException;
import com.playdata.ElectronicApproval.exception.ApprovalPermissionDeniedException;
import com.playdata.ElectronicApproval.exception.ApprovalProcessException;
import com.playdata.ElectronicApproval.repository.ApprovalFormRepository;
import jakarta.transaction.Transactional;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
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
@Transactional
public class ApprovalServiceImpl implements ApprovalService {

  private final NotificationServiceImpl notificationService;

  private final ApprovalFileDAO approvalFileDao;
  private final FileDAO fileDao;
  private final ApprovalLineDAO approvalLineDao;
  private final ApprovalLineDetailDAO approvalLineDetailDAO;
  private final ModelMapper modelMapper;
  private final FileDownloadService fileDownloadService;

  public void submitApproval(SubmitApprovalRequest dto, List<FileDTO> uploadedFiles) {
    log.info("dto :: {}", dto.toString());
    log.info("uploadedFiles :: {}", uploadedFiles);
    // 기존 결재 생성
    ApprovalFileEntity approvalFileEntity = modelMapper.map(dto, ApprovalFileEntity.class);
    ApprovalFileEntity savedEntity = approvalFileDao.save(approvalFileEntity);

    // 파일 DB 등록
    if (uploadedFiles != null && !uploadedFiles.isEmpty()) {
      uploadedFiles.forEach(file -> file.setApprovalFileId(savedEntity.getId()));
      fileDao.saveAll(uploadedFiles);
    }

    List<ApprovalLineEntity> approvers = createApprovalLines(savedEntity,
        dto.getApprovers() != null ? dto.getApprovers() : new ArrayList<>(),
        dto.getCompanyCode(), true);

    List<ApprovalLineEntity> referrers = createApprovalLines(savedEntity,
        dto.getReferencedIds() != null ? dto.getReferencedIds() : new ArrayList<>(),
        dto.getCompanyCode(), false);

    List<ApprovalLineEntity> savedLines = approvalLineDao.saveAll(approvers);
    approvalLineDao.saveAll(referrers);
    if (!savedLines.isEmpty()) {
      savedLines.get(0).getApprovalFile().setApprovalLineEntities(savedLines);
    }
  }

/*  // 결재 요청 제출 (나름 리팩토링된 메서드)
  public void submitApproval(SubmitApprovalRequest request) {
    log.info("Submitting Approval for DTO: {}", request);

    // 1. 문서 생성
    ApprovalFileEntity approvalFileEntity = new ApprovalFileEntity();
    approvalFileEntity.setId(request.getId());                      // 문서 ID
    approvalFileEntity.setName(request.getName());                 // 문서 제목
    approvalFileEntity.setText(request.getText());               // 문서 내용
    approvalFileEntity.setEmployeeId(request.getEmployeeId());      // 작성자
    ApprovalFileEntity savedEntity = approvalFileDao.save(approvalFileEntity);
    log.info("Approval File Created: {}", savedEntity.getId());

    //  2. 결재 라인 및 참조인 생성
    List<ApprovalLineEntity> approverLines = createApprovalLines(savedEntity,
        request.getApprovers(), request.getCompanyCode(), true);
    List<ApprovalLineEntity> referrerLines = createApprovalLines(savedEntity,
        request.getReferencedIds(), request.getCompanyCode(), false);

    //  3. 결재 라인 저장
    List<ApprovalLineEntity> savedLines = approvalLineDao.saveAll(approverLines);
    approvalLineDao.saveAll(referrerLines);
    log.info("Approval Lines Saved: {}", savedLines);

    // 4. 첫 번째 결재 라인에 전체 라인 정보 설정
    if (!savedLines.isEmpty()) {
      savedLines.get(0).getApprovalFile().setApprovalLineEntities(savedLines);
    }
  }*/

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
  @Transactional
  public void approveUpdateStatus(String approvalLineId, String approveOrNot, String reason) {

    ApprovalLineEntity lineEntity = approvalLineDao.findById(approvalLineId)
        .orElseThrow(() -> new ApprovalLineNotFoundException("결재 라인을 찾을 수 없습니다."));

    //   이미 처리되었는지 검증
    if (lineEntity.getApprovalLineDetail().getStatus() != ApprovalStatus.PENDING) {
      throw new ApprovalAlreadyProcessedException("이미 처리된 결재입니다.");
    }

    ApprovalStatus status = ApprovalStatus.valueOf(approveOrNot);

    List<ApprovalLineEntity> lines = lineEntity.getApprovalFile().getApprovalLineEntities();
    lines.sort(Comparator.comparingInt(ApprovalLineEntity::getApprovalOrder));
    int currentIndex = lines.indexOf(lineEntity);

    //   결재 순서 검증
    for (int i = 0; i < currentIndex; i++) {
      ApprovalStatus prevStatus = lines.get(i).getApprovalLineDetail().getStatus();
      if (prevStatus != ApprovalStatus.APPROVED) {
        throw new ApprovalProcessException("이전 결재자의 승인이 완료되지 않았습니다.");
      }
    }

    //   결재 상태 업데이트
    lineEntity.getApprovalLineDetail().setStatus(status);
    lineEntity.getApprovalLineDetail().setReason(reason);
    approvalLineDetailDAO.save(lineEntity.getApprovalLineDetail());

    //   최종 결재자인 경우 결재 파일 상태 변경
    boolean isLastApprover = currentIndex == lines.size() - 1;
    if (status == ApprovalStatus.REJECTED || (status == ApprovalStatus.APPROVED
        && isLastApprover)) {
      lineEntity.getApprovalFile().setStatus(status);
      approvalFileDao.save(lineEntity.getApprovalFile());
    }

    //  todo :: 알림 발송

    log.info("결재 처리 완료: lineId={}, status={}", approvalLineId, status);
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
  public ResponseApprovalFileDTO getApprovalFile(String approvalFileId) {
    List<FileDTO> fileDTOs = fileDownloadService.loadAllFiles(approvalFileId);
    ApprovalFileEntity approvalFile = approvalFileDao.findById(approvalFileId)
        .orElseThrow(() -> new IllegalArgumentException("Approval file not found"));
    ResponseApprovalFileDTO dto = new ResponseApprovalFileDTO();
    dto.setId(approvalFile.getId());
    dto.setName(approvalFile.getName());
    dto.setText(approvalFile.getText());
    dto.setCompanyCode(approvalFile.getCompanyCode());
    dto.setEmployeeId(approvalFile.getEmployeeId());
    dto.setStatus(approvalFile.getStatus().name());
    dto.setDeleteStatus(approvalFile.getDeleteStatus().name());
    dto.setDeleted(approvalFile.isDeleted());
    dto.setFiles(fileDTOs);
    return dto;
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
