package com.playdata.ElectronicApproval.controller;

import com.playdata.ElectronicApproval.dto.ApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.RequestApprovalFileDTO;
import com.playdata.ElectronicApproval.service.ApprovalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/approve")
public class ApprovalController {

  private final ApprovalService approvalService;

  @PostMapping("/submit") // 결재 요청 제출
  public void submitApproval(
      @RequestParam("employeeId") String employeeId,
      @RequestParam("companyCode") String companyCode,
      @RequestBody RequestApprovalFileDTO dto,
      @RequestParam("approvaers") List<String> approvaers,
      @RequestParam("referencedIds") List<String> referrers
  ) {
    approvalService.submitApproval(employeeId, companyCode, dto, approvaers, referrers);
  }

  @GetMapping("/permit") // 결재 승인/반려 처리
  public void permitApproval(@RequestParam("lineId") String lineId,
      @RequestParam("approveOrNot") String approveOrNot,
      @RequestParam("reason") String reason) {
    approvalService.approveUpdateStatus(lineId, approveOrNot, reason);
  }

  @GetMapping("/list") // 결재 문서 조회
  public List<ApprovalFileDTO> getApprovalFiles(@RequestParam("employeeId") String employeeId,
      @RequestParam("menu") int menu) {
    return approvalService.getApprovalFiles(employeeId, menu);

  }

  @GetMapping("/")
  public ApprovalFileDTO getFile(@RequestParam("file") String fileId) {
    return approvalService.getApprovalFile(fileId);
  }
}
