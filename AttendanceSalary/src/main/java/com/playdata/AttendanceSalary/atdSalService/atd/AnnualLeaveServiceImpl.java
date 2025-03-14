package com.playdata.AttendanceSalary.atdSalService.atd;

import com.playdata.AttendanceSalary.atdClient.HrmFeignClient;
import com.playdata.AttendanceSalary.atdClient.hrmDTO.EmployeeResponseDTO;
import com.playdata.AttendanceSalary.atdSalDao.atd.AnnualLeaveDAO;
import com.playdata.AttendanceSalary.atdSalDao.atd.AnnualLeaveUsageDAO;
import com.playdata.AttendanceSalary.atdSalDao.sal.PositionSalaryDao;
import com.playdata.AttendanceSalary.atdSalDto.atd.AnnualLeaveDTO;
import com.playdata.AttendanceSalary.atdSalDto.atd.AnnualLeaveRequestDTO;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveEntity;
import com.playdata.AttendanceSalary.atdSalEntity.atd.AnnualLeaveUsageEntity;
import com.playdata.AttendanceSalary.atdSalEntity.atd.LeaveApprovalStatus;
import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionSalaryStepEntity;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnnualLeaveServiceImpl implements AnnualLeaveService {

  private final AnnualLeaveDAO annualLeaveDAO;
  private final HrmFeignClient hrmFeignClient;
  private final AnnualLeaveUsageDAO annualLeaveUsageDAO;
  private final PositionSalaryDao positionSalaryDao;
  private final ModelMapper modelMapper;

  @Override
  // 휴가 신청
  public void submit(String employeeId, String companyCode, AnnualLeaveRequestDTO dto) {
    //dto 받아서 entity로 변환
    //dto 의 유효성 검사 는 프론트에서
    //AnnualLeaveRequestDTO 는 usage 에 대한 dto , entity insert
    annualLeaveUsageDAO.save(convertToAnnualLeaveUsageEntity(dto));


  }

  // employee feign 메소드
  private EmployeeResponseDTO findEmployee(String employeeId) {
    return hrmFeignClient.findEmployee(employeeId);
  }

  // List<employeeId>  feign 메소드
  private List<String> findAllEmployees() {
    return hrmFeignClient.getEmployeeIds();
  }

  // 신청자 -> employeeId 로 내역 불러오기
  public List<AnnualLeaveUsageEntity> findAllByEmployeeId(String employeeId) {
    return annualLeaveUsageDAO.findByEmployeeId(employeeId);
  }


  // 관리자 단계 -> Pending인 얘들 다 불러오기 / 신청내역 불러오기
  public List<AnnualLeaveUsageEntity> findAllByCompanyCode(String companyCode, String status) {
    LeaveApprovalStatus leaveApprovalStatus = LeaveApprovalStatus.valueOf(status);
    return annualLeaveUsageDAO.findAllByCompanyCodeAndLeaveApprovalStatus(companyCode,
        leaveApprovalStatus);
  }

  private AnnualLeaveUsageEntity convertToAnnualLeaveUsageEntity(AnnualLeaveRequestDTO dto) {
    return modelMapper.map(dto, AnnualLeaveUsageEntity.class);
  }

  private AnnualLeaveEntity convertToAnnualLeaveEntity(AnnualLeaveDTO dto) {
    return modelMapper.map(dto, AnnualLeaveEntity.class);
  }


  // 관리자/ 휴가 신청 허가 -> AnnualLeaveUsage 상태 업데이트 + AnnualLeave 에서 사용 연차 증가, 남은 연차 감소
  public void approveLeave(AnnualLeaveRequestDTO dto) {
    long annualLeaveUsageId = dto.getAnnualLeaveUsageId();
    int day = dto.getStopDate().getDayOfMonth() - dto.getStartDate().getDayOfMonth() + 1;
    AnnualLeaveUsageEntity usage = annualLeaveUsageDAO.findById(annualLeaveUsageId)
        .orElseThrow(() -> new RuntimeException("휴가 신청 내역을 찾을 수 없습니다."));
    usage.setLeaveApprovalStatus(LeaveApprovalStatus.APPROVED);
    AnnualLeaveEntity leave = annualLeaveDAO.findById(usage.getAnnualLeaveId())
        .orElseThrow(() -> new RuntimeException("가능한 연차을 찾을 수 없습니다"));
    leave.setRemainingLeave(leave.getRemainingLeave() - day);
    leave.setUsedLeave(leave.getUsedLeave() + day);
    annualLeaveUsageDAO.save(usage);
    annualLeaveDAO.save(leave);
  }

  // 관리자 / 휴가 신청 반려 -> AnnualLeaveUsage 상태 업데이트
  public void rejectLeave(AnnualLeaveRequestDTO dto) {
    long annualLeaveUsageId = dto.getAnnualLeaveUsageId();
    AnnualLeaveUsageEntity usage = annualLeaveUsageDAO.findById(annualLeaveUsageId)
        .orElseThrow(() -> new RuntimeException("휴가 신청 내역을 찾을 수 없습니다."));
    usage.setLeaveApprovalStatus(LeaveApprovalStatus.REJECTED);
    annualLeaveUsageDAO.save(usage);

  }

  // 관리자 / 추가 연차 허가 -> AnnualLeaveUsage 상태 업데이트 + AnnualLeave 에서 추가 연차 증가, (사용 연차 증가),((남은 연차 감소))
  public void approveAdditionalLeave(AnnualLeaveRequestDTO dto) {
    long annualLeaveUsageId = dto.getAnnualLeaveUsageId();
    int day = dto.getStopDate().getDayOfMonth() - dto.getStartDate().getDayOfMonth() + 1;
    AnnualLeaveUsageEntity usage = annualLeaveUsageDAO.findById(annualLeaveUsageId)
        .orElseThrow(() -> new RuntimeException("휴가 신청 내역을 찾을 수 없습니다."));
    usage.setLeaveApprovalStatus(LeaveApprovalStatus.APPROVED);
    AnnualLeaveEntity leave = annualLeaveDAO.findById(usage.getAnnualLeaveId())
        .orElseThrow(() -> new RuntimeException("가능한 연차을 찾을 수 없습니다"));
// //    leave.setRemainingLeave(leave.getRemainingLeave() - day);
//    leave.setUsedLeave(leave.getUsedLeave() + day);
    leave.setAdditionalLeave(leave.getAdditionalLeave() + day);

    annualLeaveUsageDAO.save(usage);
    annualLeaveDAO.save(leave);

  }


  // 휴가 등록 - 매달 발생
  @Scheduled(cron = "0 0 0 1 * ?")
  public void executeLeaveGrant() {
    log.info("==== 연차/월차 자동 지급 배치 시작 ====");

    // 1. 모든 직원 리스트 조회 (Feign 호출 또는 내부 서비스)
    List<String> employeeIdList = hrmFeignClient.getEmployeeIds();

    // 2. 직원별로 처리
    for (String employeeId : employeeIdList) {
      try {
        grantLeaveToEmployee(employeeId);
      } catch (Exception e) {
        log.error("연차 지급 중 오류 발생 - 사원 ID: {}", employeeId, e);
      }
    }
    log.info("==== 연차/월차 자동 지급 배치 종료 ====");
  }


  private void grantLeaveToEmployee(String employeeId) {
    // feign으로 employee 에서 입사 일시 받아와서 재직 기간 계산
    LocalDate hireDate = findEmployee(employeeId).getHireDate();
    String companyCode = findEmployee(employeeId).getCompanyCode();
    LocalDate today = LocalDate.now();

    Period period = Period.between(hireDate, today);
    int totalMonths = period.getYears() * 12 + period.getMonths();
    int yearsWorked = period.getYears();

    // 연차 엔티티 생성 (새로 지급하기 위한 객체)
    AnnualLeaveDTO dto = new AnnualLeaveDTO();
    dto.setEmployeeId(employeeId);
    dto.setCompanyCode(companyCode);

    // 1년 미만 재직자 → 월차 1일 지급
    if (totalMonths < 12) {
      if (today.getDayOfMonth() == 1) { // 매월 1일에만 지급
        setMonthlyLeave(dto);
        annualLeaveDAO.save(convertToAnnualLeaveEntity(dto));
        log.info("[월차 지급] {} / {}월차 1일 지급 완료", employeeId, totalMonths);
      }
      return; // 여기서 끝냄
    }
    // 1년 이상 재직자 → 매년 입사달에 지급 (1일마다 조회하므로 입사한 달 1일에 지급)
    if (today.getMonthValue() == hireDate.getMonthValue()) {
      setAnnualLeave(dto, yearsWorked);
      annualLeaveDAO.save(convertToAnnualLeaveEntity(dto));
      log.info("[연차 지급] {} / {}년차 연차 지급 완료", employeeId, yearsWorked);
    }
  }

  private void setMonthlyLeave(AnnualLeaveDTO dto) {
    int baseLeave = 1; // 매월 1일 지급

    dto.setBaseLeave(baseLeave);
    dto.setAdditionalLeave(0);
    dto.setTotalGrantedLeave(baseLeave);
    dto.setRemainingLeave(baseLeave);
    dto.setUsedLeave(0);

    log.info("월차 지급 완료: EmployeeId={}, Leave=1일", dto.getEmployeeId());
  }


  private void setAnnualLeave(AnnualLeaveDTO dto, int yearsWorked) {
    // dto.employeeId로 employee가 가진 직급호봉 id 가져오는 통신
    long id = findEmployee(dto.getEmployeeId()).getPositionSalaryId();
    PositionSalaryStepEntity positionSalaryStepEntity = positionSalaryDao.findPositionSalaryById(
        id).orElseThrow(() -> new RuntimeException("직급호봉 id를 찾기 못했습니다. 잘못된 직급호봉 id."));
    int baseAnnualLeave = positionSalaryStepEntity.getBaseAnnualLeave();
    //positionFeignClient.getBaseAnnualLeave(dto.getEmployeeId());
    int additionalLeave = yearsWorked; // 근속연수에 따라 1년마다 1개씩 추가
    int totalGrantedLeave = baseAnnualLeave + additionalLeave;

    dto.setBaseLeave(baseAnnualLeave);
    dto.setAdditionalLeave(additionalLeave);
    dto.setTotalGrantedLeave(totalGrantedLeave);
    dto.setRemainingLeave(totalGrantedLeave);
    dto.setUsedLeave(0);

    log.info("[연차 지급 상세] employeeId={}, base={}, additional={}, total={}",
        dto.getEmployeeId(), baseAnnualLeave, additionalLeave, totalGrantedLeave);
  }
}
