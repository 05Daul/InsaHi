

package com.playdata.ElectronicApproval.service;

import com.playdata.ElectronicApproval.controller.SseController;
import com.playdata.ElectronicApproval.dao.ApprovalFileDAO;
import com.playdata.ElectronicApproval.dao.ApprovalLineDAO;
import com.playdata.ElectronicApproval.dao.ApprovalLineDetailDAO;
import com.playdata.ElectronicApproval.dao.FileDAO;
import com.playdata.ElectronicApproval.dto.ApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.ApprovalLineDTO;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ApprovalServiceImpl implements ApprovalService {

  private final NotificationServiceImpl notificationService;
  private final SseController sseController;

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
      ApprovalLineEntity firstorder = savedLines.get(0);
      firstorder.getApprovalFile().setApprovalLineEntities(savedLines);
      // SSE 연결을 기다리기 위해 1초 후에 알림 전송
      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          sseController.notifyUser(firstorder.getEmployeeId(), "결재할 문서가 있습니다.");
        }
      }, 1000); // 1초 지연

    }
  }

  public boolean hasFirstPending(String employeeId) {
    List<ApprovalFileEntity> pendingFiles = approvalLineDao.findAllByEmployeeIdAndFirstPending(
            employeeId)
        .stream()
        .map(ApprovalLineEntity::getApprovalFile)
        .toList();

    return !pendingFiles.isEmpty();
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
  public void approveUpdateStatus(String approvalLineId, ApprovalStatus approveOrNot,
      String reason) {

    // 결재 라인 조회
    ApprovalLineEntity lineEntity = approvalLineDao.findById(approvalLineId)
        .orElseThrow(() -> new ApprovalLineNotFoundException("결재 라인을 찾을 수 없습니다."));
    log.info("결재 라인 조회");

    // 이미 처리되었는지 검증
    validateAlreadyProcessed(lineEntity);
    log.info("이미 처리되었는지 검증");

    // 결재 순서 검증
    validateApprovalOrder(lineEntity);
    log.info("결재 순서 검증");

    // 결재 상태 업데이트
    updateApprovalLineDetail(lineEntity, approveOrNot, reason);
    log.info("결재 상태 업데이트");

    // 최종 결재자 처리
    processFinalApproval(lineEntity, approveOrNot);
    log.info("최종 결재자 처리");

    String message = "결재가 처리되었습니다. 상태: " + approveOrNot;
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        sseController.notifyUser(lineEntity.getEmployeeId(), message);
      }
    }, 1000); // 1초 지연

    log.info("결재 처리 완료: lineId={}, status={}", approvalLineId, approveOrNot);

  }


  private void validateAlreadyProcessed(ApprovalLineEntity lineEntity) {
    log.info("validateAlreadyProcessed(lineEntity) :::: {}", lineEntity.getId());
    ApprovalStatus currentStatus = lineEntity.getApprovalLineDetail().getStatus();
    if (currentStatus != ApprovalStatus.PENDING) {
      throw new ApprovalAlreadyProcessedException("이미 처리된 결재입니다.");
    }
    log.info("validateAlreadyProcessed :::: process end");
  }

  private void validateApprovalOrder(ApprovalLineEntity currentLine) {
    log.info("validateApprovalOrder(ApprovalLineEntity) :::: {}", currentLine.getApprovalOrder());
    List<ApprovalLineEntity> lines = currentLine.getApprovalFile().getApprovalLineEntities();
    lines.sort(Comparator.comparingInt(ApprovalLineEntity::getApprovalOrder));

    int currentIndex = lines.indexOf(currentLine);

    for (int i = 0; i < currentIndex; i++) {
      ApprovalStatus prevStatus = lines.get(i).getApprovalLineDetail().getStatus();
      if (prevStatus == ApprovalStatus.PENDING) {
        throw new ApprovalProcessException("이전 결재자의 승인이 완료되지 않았습니다.");
      }
    }
    log.info("validateApprovalOrder :::: process end");
    // 다음 결재자 알림
    ApprovalLineEntity nextLine = findNextApprover(lines, currentIndex);
    if (nextLine != null) {
      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          sseController.notifyUser(nextLine.getEmployeeId(), "결재할 문서가 있습니다.");
        }
      }, 1000); // 1초 지연
    }

  }

  private void updateApprovalLineDetail(ApprovalLineEntity lineEntity, ApprovalStatus status,
      String reason) {
//    lineEntity.getApprovalLineDetail().getStatus();
    ApprovalLineDetailEntity detail = lineEntity.getApprovalLineDetail();
    detail.setStatus(status);
    detail.setReason(reason);

    approvalLineDetailDAO.save(detail);
    log.info("updateApprovalLineDetail ::::: process end");

  }

  private ApprovalLineEntity findNextApprover(List<ApprovalLineEntity> approvalLines,
      int currentIndex) {
    // 정렬 보장 (필요 시 다시 정렬)
    approvalLines.sort(Comparator.comparingInt(ApprovalLineEntity::getApprovalOrder));

    log.info("현재 결재자 인덱스: {}", currentIndex);

    // 현재 인덱스 이후부터 순회
    for (int i = currentIndex + 1; i < approvalLines.size(); i++) {
      ApprovalLineEntity nextLine = approvalLines.get(i);
      ApprovalStatus nextStatus = nextLine.getApprovalLineDetail().getStatus();

      log.info("다음 순서 인덱스: {}, 상태: {}", i, nextStatus);

      // 대기 상태라면 해당 결재자가 다음 차례
      if (nextStatus == ApprovalStatus.PENDING) {
        log.info("다음 결재자는 {} (결재 순서: {})", nextLine.getEmployeeId(), nextLine.getApprovalOrder());
        return nextLine;
      }
    }
    // 다음 결재자가 없음 (최종 결재자까지 승인/반려됨)
    log.info("더 이상 결재할 사람이 없습니다.");
    return null;

  }


  private void processFinalApproval(ApprovalLineEntity currentLine, ApprovalStatus status) {
    log.info("processFinalApproval(ApprovalLineEntity) :::: {}",
        currentLine.getApprovalLineDetail().getStatus().name());
    log.info("processFinalApproval(ApprovalLineEntity) :::: {}", status.name());
    List<ApprovalLineEntity> lines = currentLine.getApprovalFile().getApprovalLineEntities();
    lines.sort(Comparator.comparingInt(ApprovalLineEntity::getApprovalOrder));

    int currentIndex = lines.indexOf(currentLine);
    boolean isLastApprover = currentIndex == lines.size() - 1;

    if (status == ApprovalStatus.REJECTED || (status == ApprovalStatus.APPROVED
        && isLastApprover)) {
      currentLine.getApprovalFile().setStatus(status);
      approvalFileDao.save(currentLine.getApprovalFile());
    }
// 기안자 알림
    if (isLastApprover && status == ApprovalStatus.APPROVED) {
      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          sseController.notifyUser(currentLine.getApprovalFile().getEmployeeId(), "문서가 승인되었습니다.");
        }
      }, 1000); // 1초 지연
    }

    if (status == ApprovalStatus.REJECTED) {
      new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
          sseController.notifyUser(currentLine.getApprovalFile().getEmployeeId(), "문서가 반려되었습니다.");
        }
      }, 1000); // 1초 지연

    }

  }


  // 결재 문서 조회
  public Page<ApprovalFileDTO> getApprovalFiles(String employeeId, int menu, Pageable pageable) {
    Page<ApprovalFileEntity> filesPage;
    // menu 값에 따라 분기 처리
    if (menu == 1) {
      // 본인이 작성한 문서
      List<ApprovalFileEntity> myfiles = approvalFileDao.findAllByEmployeeId(employeeId);
      filesPage = convertListToPage(myfiles, pageable);

    } else if (menu == 2) {
      // 결재자로 지정된 문서
      List<ApprovalFileEntity> assignedFiles = findByAssignedApproval(employeeId);
      filesPage = convertListToPage(assignedFiles, pageable);

    } else if (menu == 3) {
      // 결재해야 할 문서
      List<ApprovalFileEntity> pendingFiles = firstpendingFiles(employeeId);
      filesPage = convertListToPage(pendingFiles, pageable);

    } else if (menu == 4) {
      // 참조자로 지정된 문서
      List<ApprovalFileEntity> referencedFiles = findByReferencedDocuments(employeeId);
      filesPage = convertListToPage(referencedFiles, pageable);
    } else {
      // 기본 빈 페이지 반환
      filesPage = Page.empty(pageable);
    }

    // 엔티티 -> DTO 변환
    return filesPage.map(this::convertToDto);
  }

  private Page<ApprovalFileEntity> convertListToPage(List<ApprovalFileEntity> list,
      Pageable pageable) {
    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), list.size());

    List<ApprovalFileEntity> subList =
        (start >= list.size()) ? List.of() : list.subList(start, end);

    return new PageImpl<>(subList, pageable, list.size());
  }

  @Override
  public ResponseApprovalFileDTO getApprovalFile(String approvalFileId) {
    // 1. 파일 정보 조회
    List<FileDTO> fileDTOs = fileDownloadService.loadAllFiles(approvalFileId);

    // 2. 결재 문서 정보 조회
    ApprovalFileEntity approvalFile = approvalFileDao.findById(approvalFileId)
        .orElseThrow(() -> new IllegalArgumentException("Approval file not found"));

    // 결재 라인 및 상태 정보 조회
    List<ApprovalLineDTO> approvalLines = approvalLineDao.findAllByApprovalFile(approvalFile)
        .stream()
        .map(line -> new ApprovalLineDTO(
            line.getId(),
            line.getApprovalOrder(),
            line.getEmployeeId(),
            line.getApprovalLineDetail().getStatus().name() // 결재 상태 추가
        ))
        .collect(Collectors.toList());
/*
    // 3. 결재 라인 정보 조회
    List<ApprovalLineEntity> approvalLines = approvalLineDao.findAllByApprovalFile(approvalFile);

    // 4. 결재자 순서와 employeeId 정보를 포함한 ApprovalLine 리스트 생성
    List<ApprovalLineDTO> approvalLineDTOs = approvalLines.stream()
        .map(line -> new ApprovalLineDTO(line.getId(), line.getApprovalOrder(),
            line.getEmployeeId()))
        .collect(Collectors.toList());
*/

    ResponseApprovalFileDTO dto = getResponseApprovalFileDTO(
        approvalFile, fileDTOs, approvalLines);

    return dto;
  }

  private static ResponseApprovalFileDTO getResponseApprovalFileDTO(ApprovalFileEntity approvalFile,
      List<FileDTO> fileDTOs, List<ApprovalLineDTO> approvalLineDTOs) {
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
    dto.setApprovalLines(approvalLineDTOs); // 결재 라인 정보 추가
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
    return approvalLineDao.findAllByEmployeeIdAndApprovalOrderNotZero(employeeId).stream()
        .map(line -> line.getApprovalFile())
        .collect(Collectors.toList());
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

  private List<ApprovalFileEntity> findByReferencedDocuments(String employeeId) {
    return approvalLineDao.findAllByEmployeeIdAndApprovalStatus(employeeId,
            ApprovalStatus.REFERENCES)
        .stream()
        .map(ApprovalLineEntity::getApprovalFile)
        .collect(Collectors.toList());
  }
}
