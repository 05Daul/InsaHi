package com.playdata.AttendanceSalary.atdSalController.atd;

import com.playdata.AttendanceSalary.atdSalDto.atd.AnnualLeaveDTO;
import com.playdata.AttendanceSalary.atdSalDto.atd.AnnualLeaveRequestDTO;
import com.playdata.AttendanceSalary.atdSalDto.atd.PageResponseDTO;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveEntity;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import com.playdata.AttendanceSalary.atdSalService.atd.AnnualLeaveService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leave")
@RequiredArgsConstructor
@Slf4j
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

  @GetMapping("/remaining/{employeeId}")
  public ResponseEntity<AnnualLeaveDTO> getRemainingLeave(
      @PathVariable("employeeId") String employeeId) {
    try {
      AnnualLeaveDTO latestLeave = annualLeaveService.findLatestAnnualLeave(employeeId);
      return ResponseEntity.ok(latestLeave);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(null);
    }
  }

  // 직원의 휴가 신청 내역 조회
  @GetMapping("/getemployeeleavel/{employeeId}/{status}")
  public ResponseEntity<List<AnnualLeaveRequestDTO>> getLeaveUsageByEmployeeId(
      @PathVariable("employeeId") String employeeId, @PathVariable("status") String status) {
    log.info("employeeId::{}", employeeId);
    log.info("status::{}", status);
    try {
      List<AnnualLeaveRequestDTO> usageList = annualLeaveService.findAllByEmployeeIdAndLeaveApprovalStatus(
          employeeId, status);
      log.info("usageList:size:{}//usageList:get(0).getAnnualLeaveUsageId:{}", usageList.size(),
          usageList.get(0).getAnnualLeaveUsageId());
      return ResponseEntity.ok(usageList);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(null);
    }
  }

  @GetMapping("/getmyleave/{employeeId}/{status}")
  public ResponseEntity<PageResponseDTO<AnnualLeaveRequestDTO>> getLeaveUsageByEmployeeId(
      @PathVariable("employeeId") String employeeId,
      @PathVariable("status") String status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "createDate,desc") String sort
  ) {
    try {
      Page<AnnualLeaveRequestDTO> usagePage = annualLeaveService
          .findAllByEmployeeIdAndLeaveApprovalStatusWithPagination(employeeId, status, page, size,
              sort);

      PageResponseDTO<AnnualLeaveRequestDTO> response = PageResponseDTO.<AnnualLeaveRequestDTO>builder()
          .content(usagePage.getContent())
          .page(usagePage.getNumber())
          .size(usagePage.getSize())
          .totalPages(usagePage.getTotalPages())
          .totalElements(usagePage.getTotalElements())
          .build();

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      e.printStackTrace(); // 디버깅용 로그
      return ResponseEntity.status(500).body(null);
    }
  }


  // 회사 내에서 특정 상태의 휴가 신청 내역 조회 (관리자용)
//  @GetMapping("/usage/{companyCode}/{status}")
  public ResponseEntity<List<AnnualLeaveRequestDTO>> getLeaveUsageByCompanyCode(
      @PathVariable String companyCode, @PathVariable String status) {
    try {
      List<AnnualLeaveRequestDTO> usageList = annualLeaveService.findAllByCompanyCodeAndLeaveApprovalStatus(
          companyCode, status);
      return ResponseEntity.ok(usageList);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(null);
    }
  }


  @GetMapping("/usage/{companyCode}/{status}")
  public ResponseEntity<PageResponseDTO<AnnualLeaveRequestDTO>> getLeaveUsageByCompanyWithPagination(
      @PathVariable("companyCode") String companyCode, @PathVariable("status") String status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "createDate,desc") String sort
  ) {
    try {
      log.info("companyCode:{},status:{}", companyCode, status);
      Page<AnnualLeaveRequestDTO> usageList = annualLeaveService.findAllByCompanyCodeAndLeaveApprovalStatusWithPagination(
          companyCode, status, page, size, sort);
      log.info("usageList:{}", usageList);
      PageResponseDTO<AnnualLeaveRequestDTO> response = PageResponseDTO.<AnnualLeaveRequestDTO>builder()
          .content(usageList.stream().toList())
          .page(usageList.getNumber())
          .size(usageList.getSize())
          .totalPages(usageList.getTotalPages())
          .totalElements(usageList.getTotalElements())
          .build();
      log.info("response:{}", response);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      e.printStackTrace(); // 예외 확인 필수!!
      log.info(e.getMessage());
      return ResponseEntity.status(500).body(null);
    }
  }

  @GetMapping("/detail/{id}")
  public ResponseEntity<AnnualLeaveRequestDTO> getLeaveDetailById(@PathVariable("id") Long id) {
    log.info("getLeaveDetailById :: {}", id);
    try {
      AnnualLeaveRequestDTO dto = annualLeaveService.findById(id);
      log.info("dto ::{}", dto);
      return ResponseEntity.ok(dto);
    } catch (Exception e) {
      return ResponseEntity.status(404).body(null);
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
  @PutMapping("/additional")
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
