package com.playdata.ElectronicApproval.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playdata.ElectronicApproval.common.publicEntity.FileEntity;
import com.playdata.ElectronicApproval.dto.ApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.FileDTO;
import com.playdata.ElectronicApproval.dto.ResponseApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.SubmitApprovalRequest;
import com.playdata.ElectronicApproval.service.ApprovalFileUploadServiceImpl;
import com.playdata.ElectronicApproval.service.ApprovalService;
import com.playdata.ElectronicApproval.service.FileDownloadService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
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
  public void submitApproval(@RequestBody SubmitApprovalRequest request) {
    approvalService.submitApproval(request);
  }

  @GetMapping("/permit") // 결재 승인/반려 처리
  public void permitApproval(@RequestParam("lineId") String lineId,
      @RequestParam("approveOrNot") String approveOrNot,
      @RequestParam("reason") String reason) {
    approvalService.approveUpdateStatus(lineId, approveOrNot, reason);
  }

  @GetMapping("/list/{employeeId}/{menu}") // 결재 문서 조회
  public List<ApprovalFileDTO> getApprovalFiles(@PathVariable("employeeId") String employeeId,
      @PathVariable("menu") int menu) {
    return approvalService.getApprovalFiles(employeeId, menu);

  }

  @GetMapping("/file/{approvalFileId}")
  public ResponseApprovalFileDTO getFile(@PathVariable("approvalFileId") String approvalFileId) {
    // 결재문서를 가져오는 메소드
    return approvalService.getApprovalFile(approvalFileId);
  }

  @GetMapping("/file/download/{fileId}")
  public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Long fileId) {
    return fileDownloadService.downloadFile(fileId);
  }
}
