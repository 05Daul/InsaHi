package com.playdata.ElectronicApproval.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playdata.ElectronicApproval.common.publicEntity.FileEntity;
import com.playdata.ElectronicApproval.dto.ApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.FileDTO;
import com.playdata.ElectronicApproval.dto.PermitApprovalRequest;
import com.playdata.ElectronicApproval.dto.ResponseApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.SubmitApprovalRequest;
import com.playdata.ElectronicApproval.service.ApprovalFileUploadServiceImpl;
import com.playdata.ElectronicApproval.service.ApprovalService;
import com.playdata.ElectronicApproval.service.FileDownloadService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/approval")
@Slf4j
public class ApprovalController {

  private final ApprovalService approvalService;
  private final ApprovalFileUploadServiceImpl fileUploadService;
  private final FileDownloadService fileDownloadService;

  @PostMapping("/submit")
  public void submittest2(
      @RequestParam("jsonData") String jsonData,
      @RequestPart("files") List<MultipartFile> files) {

    log.info("files :: {}", files);

    List<FileDTO> uploadedFiles = null;
    if (files != null && !files.isEmpty()) {
      try {
        uploadedFiles = fileUploadService.uploadFiles(files);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    // ObjectMapper로 JSON 문자열을 SubmitApprovalRequest로 변환
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      SubmitApprovalRequest dto = objectMapper.readValue(jsonData, SubmitApprovalRequest.class);
      log.info("jsonData :: {}", dto);
      approvalService.submitApproval(dto, uploadedFiles);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

  }
//  public void submittest(@ModelAttribute SubmitApprovalRequest submitApprovalRequest) {
//    System.out.println(submitApprovalRequest);
//  }

  public ResponseEntity<?> submitApprovalWithFile(
      @RequestPart("jsonData") SubmitApprovalRequest dto,
      @RequestPart(value = "files", required = false) List<MultipartFile> files)
      throws IOException {

    log.info("jsonData :: {}", dto);
    log.info("files :: {}", files);

    List<FileDTO> uploadedFiles = null;
    if (files != null && !files.isEmpty()) {
      uploadedFiles = fileUploadService.uploadFiles(files);
    }

    approvalService.submitApproval(dto, uploadedFiles);

    return ResponseEntity.ok("결재문서 + 파일 업로드 성공");
  }

  //  @PostMapping("/submit") // 결재 요청 제출
//  public void submitApproval(@RequestBody SubmitApprovalRequest request) {
//    approvalService.submitApproval(request);
//  }

  @PostMapping("/permit") // 결재 승인/반려 처리
  public ResponseEntity<String> permitApproval(@RequestBody PermitApprovalRequest request) {
    log.info("결재 처리 요청 수신: lineId={}, approveOrNot={}, reason={}", request.getLineId(),
        request.getApproveOrNot(), request.getReason());
    try {
      approvalService.approveUpdateStatus(
          request.getLineId(),
          request.getApproveOrNot(),
          request.getReason()
      );
      return ResponseEntity.ok("결재가 성공적으로 처리되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("결재 처리 중 오류가 발생했습니다.");
    }

  }

  @GetMapping("/has-pending/{employeeId}")
  public Map<String, Boolean> hasFirstPendingDocument(@PathVariable String employeeId) {
    boolean hasPending = approvalService.hasFirstPending(employeeId);
    return Map.of("hasPending", hasPending);
  }

//  @GetMapping("/list/{employeeId}/{menu}") // 결재 문서 조회
//  public List<ApprovalFileDTO> getApprovalFiles(@PathVariable("employeeId") String employeeId,
//      @PathVariable("menu") int menu) {
//    return approvalService.getApprovalFiles(employeeId, menu);
//
//  }

  @GetMapping("/list/{employeeId}/{menu}")
  public Page<ApprovalFileDTO> getDocuments(@PathVariable("employeeId") String employeeId,
      @PathVariable("menu") int menu,
      Pageable pageable) {
    return approvalService.getApprovalFiles(employeeId, menu, pageable);
  }
/*
  @GetMapping("/file/{approvalFileId}")
  public ResponseApprovalFileDTO getFile(@PathVariable("approvalFileId") String approvalFileId) {
    // 결재문서를 가져오는 메소드
    return approvalService.getApprovalFile(approvalFileId);
  }*/

  @GetMapping("/file/{approvalFileId}")
  public ResponseEntity<ResponseApprovalFileDTO> getFile(
      @PathVariable("approvalFileId") String approvalFileId) {
    log.info("Request received for file with approvalFileId: {}", approvalFileId);

    try {
      // 결재 문서와 파일 정보 조회
      ResponseApprovalFileDTO response = approvalService.getApprovalFile(approvalFileId);
      log.info("file:{}", response);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new ResponseApprovalFileDTO("파일을 찾을 수 없습니다."));
    }
  }

  @GetMapping("/file/download/{fileId}")
  public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") String fileId) {
    try {
      return fileDownloadService.downloadFile(fileId);  // 서비스 호출
    } catch (Exception e) {
      // 예외 처리 로직 추가
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  public ResponseEntity<Resource> downloadFile1(@PathVariable("fileId") String fileId) {
    return fileDownloadService.downloadFile(fileId);
  }
}
