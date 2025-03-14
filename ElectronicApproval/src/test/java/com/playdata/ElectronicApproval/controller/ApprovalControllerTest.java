package com.playdata.ElectronicApproval.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ApprovalControllerTest {

  @Autowired
  ApprovalController approvalController;

//  @Test
//  public void approve() {
//
//    String employeeId = "E001";
//    String companyCode = "C001";
//    RequestApprovalFileDTO dto = new RequestApprovalFileDTO(null, "기안서", "contents", "A001",
//        companyCode,
//        employeeId);
//    List<String> approvaers = new ArrayList<>();
//    approvaers.add("E003");
//    approvaers.add("E002");
//    approvaers.add("E004");
//
//    approvalController.submitApproval(employeeId, companyCode, dto, approvaers, approvaers);
//  }
}