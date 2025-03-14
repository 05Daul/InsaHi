package com.playdata.AttendanceSalary.atdSalService.atd;

import static org.junit.jupiter.api.Assertions.*;

import com.playdata.AttendanceSalary.atdSalDto.atd.AnnualLeaveRequestDTO;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AnnualLeaveServiceImplTest {

  @Autowired
  private AnnualLeaveService annualLeaveService;

  @Test
  void
  @Test
  @DisplayName("휴가 신청 기능 테스트")
  void submitLeaveRequest() {
    // Given
    String employeeId = "E001";
    String companyCode = "C001";

    AnnualLeaveRequestDTO requestDTO = new AnnualLeaveRequestDTO();
    requestDTO.setAnnualLeaveId(1L);
    requestDTO.setStartDate(LocalDate.of(2024, 4, 10));
    requestDTO.setStopDate(LocalDate.of(2024, 4, 12));
    requestDTO.setLeaveApprovalStatus("PENDING");
    requestDTO.setReason("개인 사정");

    // When
    assertDoesNotThrow(() -> annualLeaveService.submit(employeeId, companyCode, requestDTO));

    // Then
    List<AnnualLeaveUsageEntity> usageList = annualLeaveService.findAllByEmployeeId(employeeId);
    assertNotNull(usageList);
//    assertTrue(usageList.size() > 0);
  }

  @Test
  @DisplayName("휴가 승인 기능 테스트")
  void approveLeaveTest() {
    // Given
    AnnualLeaveRequestDTO approveRequestDTO = new AnnualLeaveRequestDTO();
    approveRequestDTO.setAnnualLeaveUsageId(1L);
    approveRequestDTO.setStartDate(LocalDate.of(2024, 4, 10));
    approveRequestDTO.setStopDate(LocalDate.of(2024, 4, 12));

    // When / Then
    assertDoesNotThrow(() -> annualLeaveService.approveLeave(approveRequestDTO));
  }

  @Test
  @DisplayName("휴가 반려 기능 테스트")
  void rejectLeaveTest() {
    // Given
    AnnualLeaveRequestDTO rejectRequestDTO = new AnnualLeaveRequestDTO();
    rejectRequestDTO.setAnnualLeaveUsageId(2L);

    // When / Then
    assertDoesNotThrow(() -> annualLeaveService.rejectLeave(rejectRequestDTO));
  }

  @Test
  @DisplayName("추가 연차 승인 기능 테스트")
  void approveAdditionalLeaveTest() {
    // Given
    AnnualLeaveRequestDTO additionalLeaveDTO = new AnnualLeaveRequestDTO();
    additionalLeaveDTO.setAnnualLeaveUsageId(3L);
    additionalLeaveDTO.setStartDate(LocalDate.of(2024, 4, 15));
    additionalLeaveDTO.setStopDate(LocalDate.of(2024, 4, 16));

    // When / Then
    assertDoesNotThrow(() -> annualLeaveService.approveAdditionalLeave(additionalLeaveDTO));
  }
}