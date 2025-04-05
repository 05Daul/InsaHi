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
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    // dto 의 유효성 검사 는 프론트에서도 해야
    //AnnualLeaveRequestDTO 는 usage 에 대한 dto , entity insert
    // 중복 신청 체크
//    log.info("employeeId::{},companyCode::{},dto::{}", employeeId, companyCode, dto);
    boolean hasOverlap = annualLeaveUsageDAO.existsOverlappingLeave(employeeId, dto.getStartDate(),
        dto.getStopDate());
    log.info("hasOverlap::{}", hasOverlap);
    if (hasOverlap) {
      throw new RuntimeException("이미 해당 기간에 신청된 휴가가 존재합니다.");
    }

    // 최신 AnnualLeave 조회
    AnnualLeaveEntity latestLeave = annualLeaveDAO.findLatestByEmployeeId(employeeId)
        .orElseThrow(() -> new RuntimeException("유효한 연차 정보가 없습니다."));
    log.info("latestLeave::{}", latestLeave.getAnnualLeaveId());
    // 남은 연차 확인
    int requestedDays = dto.getStopDate().getDayOfMonth() - dto.getStartDate().getDayOfMonth() + 1;
    log.info("requestedDays::{}", requestedDays);
    if (latestLeave.getRemainingLeave() < requestedDays) {
      throw new RuntimeException("잔여 연차가 부족합니다.");
    }
    log.info("latestLeave::{}", latestLeave.getRemainingLeave());

    // 신청 정보 저장
    AnnualLeaveUsageEntity usageEntity = convertToAnnualLeaveUsageEntity(dto);
    usageEntity.setAnnualLeaveId(latestLeave.getAnnualLeaveId());

    annualLeaveUsageDAO.save(usageEntity);


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

  public List<AnnualLeaveRequestDTO> findAllByEmployeeIdAndLeaveApprovalStatus(String employeeId,
      String status) {
    LeaveApprovalStatus leaveApprovalStatus = LeaveApprovalStatus.valueOf(status);
    return annualLeaveUsageDAO.findByEmployeeIdAndLeaveApprovalStatus(employeeId,
            leaveApprovalStatus)
        .stream()
        .map(entity -> modelMapper.map(entity, AnnualLeaveRequestDTO.class))
        .collect(Collectors.toList());
  }

  public Page<AnnualLeaveRequestDTO> findAllByEmployeeIdAndLeaveApprovalStatusWithPagination(
      String employeeId, String status, int page, int size, String sort) {

    String[] sortParams = sort.split(",");
    String property = sortParams[0];
    Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
        ? Sort.Direction.DESC
        : Sort.Direction.ASC;

    Sort sorting = Sort.by(new Sort.Order(direction, property));

    Pageable pageable = PageRequest.of(page, size, sorting);

    Page<AnnualLeaveUsageEntity> pageResult;
    if ("ALL".equalsIgnoreCase(status)) {
      pageResult = annualLeaveUsageDAO.findAllByEmployeeId(employeeId, pageable);
    } else {
      LeaveApprovalStatus leaveApprovalStatus = LeaveApprovalStatus.valueOf(status);
      pageResult = annualLeaveUsageDAO.findAllByEmployeeIdAndLeaveApprovalStatus(employeeId,
          leaveApprovalStatus, pageable);
    }

    return pageResult.map(entity -> modelMapper.map(entity, AnnualLeaveRequestDTO.class));
  }


  // 관리자 단계 -> Pending인 얘들 다 불러오기 / 신청내역 불러오기
  public List<AnnualLeaveRequestDTO> findAllByCompanyCodeAndLeaveApprovalStatus(String companyCode,
      String status) {
    LeaveApprovalStatus leaveApprovalStatus = LeaveApprovalStatus.valueOf(status);
    return annualLeaveUsageDAO.findAllByCompanyCodeAndLeaveApprovalStatus(companyCode,
            leaveApprovalStatus)
        .stream()
        .map(entity -> modelMapper.map(entity, AnnualLeaveRequestDTO.class))
        .collect(Collectors.toList());
  }

  @Override
  public Page<AnnualLeaveRequestDTO> findAllByCompanyCodeAndLeaveApprovalStatusWithPagination(
      String companyCode, String status, int page, int size, String sort) {

    String[] sortParams = sort.split(",");
    String property = sortParams[0];
    Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
        ? Sort.Direction.DESC
        : Sort.Direction.ASC;

    Sort sorting = Sort.by(new Sort.Order(direction, property));

    Pageable pageable = PageRequest.of(page, size, sorting);

    Page<AnnualLeaveUsageEntity> pageResult;

    if ("ALL".equalsIgnoreCase(status)) {
      pageResult = annualLeaveUsageDAO.findAllByCompanyCode(companyCode, pageable);
    } else {
      LeaveApprovalStatus leaveApprovalStatus = LeaveApprovalStatus.valueOf(status);
      pageResult = annualLeaveUsageDAO.findAllByCompanyCodeAndLeaveApprovalStatus(companyCode,
          leaveApprovalStatus, pageable);
    }

    // Entity -> DTO 변환 (Page.map 사용)
    Page<AnnualLeaveRequestDTO> dtoPage = pageResult.map(
        entity -> modelMapper.map(entity, AnnualLeaveRequestDTO.class));

    return dtoPage;
  }

  private AnnualLeaveUsageEntity convertToAnnualLeaveUsageEntity(AnnualLeaveRequestDTO dto) {
    return modelMapper.map(dto, AnnualLeaveUsageEntity.class);
  }

  private AnnualLeaveEntity convertToAnnualLeaveEntity(AnnualLeaveDTO dto) {
    return modelMapper.map(dto, AnnualLeaveEntity.class);
  }

  public AnnualLeaveRequestDTO findById(Long id) {
    log.info("getLeaveDetailById :: {}", id);
    AnnualLeaveUsageEntity entity = annualLeaveUsageDAO.findById(id)
        .orElseThrow(() -> new RuntimeException("해당 휴가 신청이 존재하지 않습니다."));
    log.info("entity.getAnnualLeaveId :: {}", entity.getAnnualLeaveId());
    log.info("entity.getAnnualLeaveUsageId :: {}", entity.getAnnualLeaveUsageId());
    return modelMapper.map(entity, AnnualLeaveRequestDTO.class);
  }

  // 관리자/ 휴가 신청 허가 -> AnnualLeaveUsage 상태 업데이트 + AnnualLeave 에서 사용 연차 증가, 남은 연차 감소
  public void approveLeave(AnnualLeaveRequestDTO dto) {
    long annualLeaveUsageId = dto.getAnnualLeaveUsageId();
    long day = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getStopDate()) + 1;
    AnnualLeaveUsageEntity usage = annualLeaveUsageDAO.findById(annualLeaveUsageId)
        .orElseThrow(() -> new RuntimeException("휴가 신청 내역을 찾을 수 없습니다."));
    usage.setLeaveApprovalStatus(LeaveApprovalStatus.APPROVED);
    AnnualLeaveEntity leave = annualLeaveDAO.findById(usage.getAnnualLeaveId())
        .orElseThrow(() -> new RuntimeException("가능한 연차을 찾을 수 없습니다"));
    leave.setRemainingLeave((int) (leave.getRemainingLeave() - day));
    leave.setUsedLeave((int) (leave.getUsedLeave() + day));
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
    leave.setTotalGrantedLeave(leave.getTotalGrantedLeave() + day);

    annualLeaveUsageDAO.save(usage);
    annualLeaveDAO.save(leave);

  }

  private EmployeeResponseDTO safeFindEmployee(String employeeId) {
    try {
      return findEmployee(employeeId); // 내부적으로 hrmFeignClient 호출
    } catch (Exception e) {
      log.warn("Feign 호출 실패 - employeeId: {}", employeeId);
      return null;
    }
  }


  // 휴가 등록 - 매달 발생
  @Scheduled(cron = "0 0 */6 * * ?", zone = "Asia/Seoul")
  public void executeLeaveGrant() {
    log.info("==== 연차/월차 자동 지급 배치 시작 ====");

    // 1. 모든 직원 리스트 조회 (Feign 호출 또는 내부 서비스)
    List<String> employeeIdList = hrmFeignClient.getEmployeeIds();

    // 2. 유효한 사원만 필터링
    List<String> validEmployeeIds = employeeIdList.stream()
        .map(this::safeFindEmployee) // 예외 방지용 safe 조회
        .filter(e -> e != null
            && e.getHireDate() != null
            && e.getPositionSalaryId() != null)
        .map(EmployeeResponseDTO::getEmployeeId)
        .collect(Collectors.toList());

    // 2. 직원별로 처리
    for (String employeeId : validEmployeeIds) {
      try {
        grantLeaveToEmployee(employeeId);
      } catch (Exception e) {
        log.error("연차 지급 중 오류 발생 - 사원 ID: {}", employeeId, e);
      }
    }
    log.info("==== 연차/월차 자동 지급 배치 종료 ====");
  }

  @Override
  public AnnualLeaveDTO findLatestAnnualLeave(String employeeId) {
    return annualLeaveDAO.findLatestByEmployeeId(employeeId)
        .map(entity -> modelMapper.map(entity, AnnualLeaveDTO.class))
        .orElseGet(() -> {
          AnnualLeaveDTO emptyLeave = new AnnualLeaveDTO();
          emptyLeave.setAnnualLeaveId(0L);
          emptyLeave.setEmployeeId(employeeId);
          emptyLeave.setBaseLeave(0);
          emptyLeave.setAdditionalLeave(0);
          emptyLeave.setTotalGrantedLeave(0);
          emptyLeave.setRemainingLeave(0);
          emptyLeave.setUsedLeave(0);
          return emptyLeave;
        });
  }


  private void grantLeaveToEmployee(String employeeId) {
    log.info("employeeId::{}", employeeId);
    // feign으로 employee 에서 입사 일시 받아와서 재직 기간 계산
    Date hireDate = findEmployee(employeeId).getHireDate();
    LocalDate localHireDate = hireDate.toInstant()
        .atZone(ZoneId.systemDefault())  // 시스템 기본 시간대 사용
        .toLocalDate();  // LocalDate로 변환

    String companyCode = findEmployee(employeeId).getCompanyCode();
    LocalDate today = LocalDate.now();

    Period period = Period.between(localHireDate, today);
    int totalMonths = period.getYears() * 12 + period.getMonths();
    int yearsWorked = period.getYears();

    log.info("period::{}", period);
    log.info("totalMonths::{}", totalMonths);
    log.info("yearsWorked::{}", yearsWorked);

    // 연차 엔티티 생성 (새로 지급하기 위한 객체)
    AnnualLeaveDTO dto = new AnnualLeaveDTO();
    dto.setEmployeeId(employeeId);
    dto.setCompanyCode(companyCode);
    log.info("dto::{}", dto);
    // 1년 미만 재직자 → 월차 1일 지급
    if (totalMonths < 12) {
      setMonthlyLeave(dto);
      annualLeaveDAO.save(convertToAnnualLeaveEntity(dto));
      log.info("[월차 지급] {} / {}개월차 지급 완료", employeeId, totalMonths);
      return; // 여기서 끝냄
    }

    // 1년 이상 재직자 → 매년 입사달에 지급 (1일마다 조회하므로 입사한 달 1일에 지급)
    if (today.getMonthValue() == localHireDate.getMonthValue()) {
      setAnnualLeave(dto, yearsWorked);
      annualLeaveDAO.save(convertToAnnualLeaveEntity(dto));
      log.info("[연차 지급] {} / {}년차 연차 지급 완료", employeeId, yearsWorked);
    }
//    log.info("")
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
    int additionalLeave = yearsWorked; // 근속연수에 따라 1년마다 1개씩 추가
    int baseAnnualLeave = positionSalaryStepEntity.getBaseAnnualLeave() + additionalLeave;
    //positionFeignClient.getBaseAnnualLeave(dto.getEmployeeId());
    int totalGrantedLeave = baseAnnualLeave;

    dto.setBaseLeave(baseAnnualLeave);
    dto.setAdditionalLeave(0);
    dto.setTotalGrantedLeave(totalGrantedLeave);
    dto.setRemainingLeave(totalGrantedLeave);
    dto.setUsedLeave(0);

    log.info("[연차 지급 상세] employeeId={}, base={}, additional={}, total={}",
        dto.getEmployeeId(), baseAnnualLeave, additionalLeave, totalGrantedLeave);
  }
}
