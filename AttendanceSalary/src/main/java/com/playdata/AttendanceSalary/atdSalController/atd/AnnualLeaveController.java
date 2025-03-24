package com.playdata.AttendanceSalary.atdSalController.atd;

import com.playdata.AttendanceSalary.atdSalDto.atd.AnnualLeaveRequestDTO;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import com.playdata.AttendanceSalary.atdSalService.atd.AnnualLeaveService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leave")
@RequiredArgsConstructor
public class AnnualLeaveController {

  private final AnnualLeaveService annualLeaveService;
//124561585156546
  // 휴가 신청
  @PostMapping("/submit")
  public ResponseEntity<String> submitLeave(@RequestBody AnnualLeaveRequestDTO requestDTO) {
    try {
      annualLeaveService.submit(requestDTO.getEmployeeId(), requestDTO.getCompanyCode(),
          requestDTO);
      return ResponseEntity.ok("휴가 신청이 완료되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("휴가 신청 처리 중 오류가 발생했습니다.");
    }
  }

  // 직원의 휴가 신청 내역 조회
  @GetMapping("/usage/{employeeId}")
  public ResponseEntity<List<AnnualLeaveUsageEntity>> getLeaveUsageByEmployeeId(
      @PathVariable String employeeId) {
    try {
      List<AnnualLeaveUsageEntity> usageList = annualLeaveService.findAllByEmployeeId(employeeId);
      return ResponseEntity.ok(usageList);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(null);
    }
  }

  // 회사 내에서 특정 상태의 휴가 신청 내역 조회 (관리자용)
  @GetMapping("/usage/company/{companyCode}/{status}")
  public ResponseEntity<List<AnnualLeaveUsageEntity>> getLeaveUsageByCompanyCode(
      @PathVariable String companyCode, @PathVariable String status) {
    try {
      List<AnnualLeaveUsageEntity> usageList = annualLeaveService.findAllByCompanyCode(companyCode,
          status);
      return ResponseEntity.ok(usageList);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(null);
    }
  }

  // 휴가 승인
  @PutMapping("/approve")
  public ResponseEntity<String> approveLeave(@RequestBody AnnualLeaveRequestDTO requestDTO) {
    try {
      annualLeaveService.approveLeave(requestDTO);
      return ResponseEntity.ok("휴가가 승인되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("휴가 승인 중 오류가 발생했습니다.");
    }
  }

  // 휴가 반려
  @PutMapping("/reject")
  public ResponseEntity<String> rejectLeave(@RequestBody AnnualLeaveRequestDTO requestDTO) {
    try {
      annualLeaveService.rejectLeave(requestDTO);
      return ResponseEntity.ok("휴가가 반려되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("휴가 반려 중 오류가 발생했습니다.");
    }
  }

  // 추가 연차 승인
  @PutMapping("/approve/additional")
  public ResponseEntity<String> approveAdditionalLeave(
      @RequestBody AnnualLeaveRequestDTO requestDTO) {
    try {
      annualLeaveService.approveAdditionalLeave(requestDTO);
      return ResponseEntity.ok("추가 연차가 승인되었습니다.");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("추가 연차 승인 중 오류가 발생했습니다.");
    }
  }


}
