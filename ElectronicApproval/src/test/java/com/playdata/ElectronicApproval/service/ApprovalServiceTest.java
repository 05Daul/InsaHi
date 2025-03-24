package com.playdata.ElectronicApproval.service;

import com.playdata.ElectronicApproval.dto.ApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.RequestApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.SubmitApprovalRequest;
import com.playdata.ElectronicApproval.entity.ApprovalFileEntity;
import com.playdata.ElectronicApproval.entity.ApprovalStatus;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ApprovalServiceTest {

  @Autowired
  ApprovalServiceImpl approvalService;

  @Test
  public void approve() {
//    String employeeId = "E001";
    String employeeId = "E003";
    String companyCode = "C001";
    RequestApprovalFileDTO dto = new RequestApprovalFileDTO(null, "기안서", "contents", "A001",
        companyCode,
        employeeId);
    System.out.println(dto);

    List<String> approvaers = new ArrayList<>();
    approvaers.add("E003");
    approvaers.add("E002");
    approvaers.add("E004");

    List<String> referrers = new ArrayList<>();
    referrers.add("A005");
    referrers.add("A006");
    List<MultipartFile> files = new ArrayList<>();
    SubmitApprovalRequest request = new SubmitApprovalRequest(null, "기안서", "contents",
        companyCode,
        employeeId, approvaers, referrers);
  }


  @Test
  public void approveFile() {
    String fileId = "47bd94eb-c4fc-4e86-933b-9a533d789a66";
    approvalService.getApprovalFile(fileId);
    System.out.println(approvalService.getApprovalFile(fileId).getClass());
    System.out.println(approvalService.getApprovalFile(fileId));
  }

  @Test
  public void approve4() {
    String employeeId = "E002";
    System.out.println("-----------------------------------");
    List<ApprovalFileDTO> files1 = approvalService.getApprovalFiles(employeeId, 1);
    System.out.println(files1);
    System.out.println("-----------------------------------");
    List<ApprovalFileDTO> files2 = approvalService.getApprovalFiles(employeeId, 2);
    System.out.println(files2);
    System.out.println("---------------------------------");
    List<ApprovalFileDTO> files3 = approvalService.getApprovalFiles(employeeId, 3);
    System.out.println(files3);
    // 조회하고 일단 파일이 가진 라인 중 employeeId가 일치하고  return
  }

  @Test
  public void approve2() {
//    String approvalLineId = "";
    String approvalLineId = "f9874012-df2b-40cd-a554-75d8a61b3d04_line_1";
    ApprovalStatus approved = ApprovalStatus.APPROVED;
    String reason = "reason";

    approvalService.approveUpdateStatus(approvalLineId, approved, reason);
  }

  @Test
  public void approve22() {
    String approvalLineId = "93aef41c-714d-4488-820f-0d219a583dd6_line_2";
    ApprovalStatus rejected = ApprovalStatus.REJECTED;
    String reason = "reason";

    approvalService.approveUpdateStatus(approvalLineId, rejected, reason);
  }

  @Test
  public void approve3() {
    String approvalLineId = "93aef41c-714d-4488-820f-0d219a583dd6_line_2";
    ApprovalStatus rejected = ApprovalStatus.REJECTED;
    String reason = "reason";

    approvalService.approveUpdateStatus(approvalLineId, rejected, reason);
  }
}