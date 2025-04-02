package com.playdata.AttendanceSalary.atdSalService.atd;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.playdata.AttendanceSalary.atdSalDto.atd.AnnualLeaveDTO;
import com.playdata.AttendanceSalary.atdSalDto.atd.AnnualLeaveRequestDTO;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveEntity;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import com.playdata.AttendanceSalary.atdSalRepository.atd.AnnualLeaveRepository;
import com.playdata.AttendanceSalary.atdSalRepository.atd.AnnualLeaveUsageRepository;
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

  @Autowired
  private AnnualLeaveRepository annualLeaveRepository;
  @Autowired
  private AnnualLeaveUsageRepository annualLeaveUsageRepository;

  @Test
  @DisplayName("직원에게 연차 지급 데이터 생성 테스트")
  void createAnnualLeaveData() {
    // given
    String employeeId = "2025ec06"; //
    String employeeId1 = "123"; //
    String employeeId2 = "111"; //
    String employeeId3 = "1234"; //
    String employeeId4 = "1235"; //
    String employeeId5 = "1236"; //
    String companyCode = "Com235e";

    AnnualLeaveEntity annualLeave = AnnualLeaveEntity.builder()
        .employeeId(employeeId)
        .companyCode(companyCode)
        .baseLeave(15)
        .remainingLeave(15)
        .additionalLeave(0)
        .usedLeave(0)
        .totalGrantedLeave(15)
        .build();

    // when
    annualLeaveRepository.save(annualLeave);

//    // then
//    AnnualLeaveEntity savedLeave = annualLeaveRepository.findById(annualLeave.getAnnualLeaveId())
//        .orElse(null);
//    assertThat(savedLeave).isNotNull();
//    assertThat(savedLeave.getRemainingLeave()).isEqualTo(15);
  }

  @Test
  @DisplayName("잔여 연차 조회 테스트")
  void findLatestAnnualLeave() {
    // given
    String employeeId = "2025b2c9";
    String companyCode = "Comb013";

//    AnnualLeaveEntity annualLeave = AnnualLeaveEntity.builder()
//        .employeeId(employeeId)
//        .companyCode(companyCode)
//        .baseLeave(15)
//        .remainingLeave(15)
//        .additionalLeave(0)
//        .usedLeave(0)
//        .totalGrantedLeave(15)
//        .build();
//
//    annualLeaveRepository.save(annualLeave);

    // when
    AnnualLeaveDTO latestLeave = annualLeaveService.findLatestAnnualLeave(employeeId);

    // then
    assertThat(latestLeave).isNotNull();
    assertThat(latestLeave.getRemainingLeave()).isEqualTo(15);
  }

  @Test
  @DisplayName("휴가 신청 및 사용 내역 저장 테스트")
  void submitAnnualLeave() {
    // given
    String employeeId = "2025b2c9";
    String companyCode = "Comb013";

    // 먼저 연차 데이터 생성
    AnnualLeaveEntity annualLeave = AnnualLeaveEntity.builder()
        .employeeId(employeeId)
        .companyCode(companyCode)
        .baseLeave(15)
        .remainingLeave(15)
        .additionalLeave(0)
        .usedLeave(0)
        .totalGrantedLeave(15)
        .build();

    annualLeaveRepository.save(annualLeave);

    // when: 휴가 신청 실행
    annualLeaveService.submit(employeeId, companyCode, AnnualLeaveRequestDTO.builder()
        .employeeId(employeeId)
        .companyCode(companyCode)
        .startDate(LocalDate.of(2025, 4, 1))
        .stopDate(LocalDate.of(2025, 4, 2))
        .reason("개인 사정")
        .build());

    // then: 신청 내역 존재 여부 확인
    AnnualLeaveUsageEntity usage = annualLeaveUsageRepository.findAll().stream()
        .filter(u -> u.getEmployeeId().equals(employeeId))
        .findFirst()
        .orElse(null);

    assertThat(usage).isNotNull();
    assertThat(usage.getLeaveApprovalStatus()).isEqualTo("PENDING");
    assertThat(usage.getStartDate()).isEqualTo(LocalDate.of(2025, 4, 1));
  }


  @Test
  @DisplayName("휴가 신청 기능 테스트")
  void submitLeaveRequest() {
    // Given
    String employeeId = "2025b2c9";
    String companyCode = "Comb013";
    String status = "Pending";

    AnnualLeaveRequestDTO requestDTO = new AnnualLeaveRequestDTO();
    requestDTO.setAnnualLeaveId(1L);
    requestDTO.setStartDate(LocalDate.of(2024, 4, 10));
    requestDTO.setStopDate(LocalDate.of(2024, 4, 12));
    requestDTO.setLeaveApprovalStatus(status);
    requestDTO.setReason("개인 사정");

    // When
    annualLeaveService.submit(employeeId, companyCode, requestDTO);

    // Then
    List<AnnualLeaveRequestDTO> usageList = annualLeaveService.findAllByEmployeeIdAndLeaveApprovalStatus(
        employeeId, status);
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