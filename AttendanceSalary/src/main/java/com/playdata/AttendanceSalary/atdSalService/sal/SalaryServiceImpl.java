package com.playdata.AttendanceSalary.atdSalService.sal;

import com.playdata.AttendanceSalary.atdClient.HrmFeignClient;
import com.playdata.AttendanceSalary.atdClient.hrmDTO.EmployeeResponseDTO;
import com.playdata.AttendanceSalary.atdSalDao.sal.AllowanceDao;
import com.playdata.AttendanceSalary.atdSalDao.sal.DeductionDao;
import com.playdata.AttendanceSalary.atdSalDao.sal.EmployeeAllowDao;
import com.playdata.AttendanceSalary.atdSalDao.sal.PayStubDao;
import com.playdata.AttendanceSalary.atdSalDao.sal.PositionDao;
import com.playdata.AttendanceSalary.atdSalDao.sal.PositionSalaryDao;
import com.playdata.AttendanceSalary.atdSalDto.sal.AllowanceResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.DeductionResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.EmployeeAllowDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.PayStubResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.PositionResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.PositionSalaryStepResponseDTO;
import com.playdata.AttendanceSalary.atdSalEntity.sal.AllowanceEntity;
import com.playdata.AttendanceSalary.atdSalEntity.sal.DeductionEntity;
import com.playdata.AttendanceSalary.atdSalEntity.sal.DeductionType;
import com.playdata.AttendanceSalary.atdSalEntity.sal.EmployeeAllowEntity;
import com.playdata.AttendanceSalary.atdSalEntity.sal.PayStubEntity;
import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionEntity;
import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionSalaryStepEntity;
import com.playdata.AttendanceSalary.atdSalService.atd.AttendanceServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalaryServiceImpl implements SalaryService {

  private final PositionDao positionDao;
  private final PositionSalaryDao positionSalaryDao;
  private final ModelMapper modelMapper;
  private final AllowanceDao allowanceDao;
  private final DeductionDao deductionDao;
  private final PayStubDao payStubDao;
  private final EmployeeAllowDao employeeAllowDao;
  private final HrmFeignClient hrmFeignClient;
  private final AttendanceServiceImpl attendanceServiceImpl;

  public EmployeeResponseDTO updatePositionSalary(EmployeeResponseDTO employeeResponse) {

    EmployeeResponseDTO employeeResponseDTO = hrmFeignClient.findEmployee(
        employeeResponse.getEmployeeId());

    Optional<PositionSalaryStepEntity> settingSalaryStep =
        positionSalaryDao.findPositionSalaryById(employeeResponse.getPositionSalaryId());

    settingSalaryStep.ifPresent(positionSalaryStep ->
        employeeResponseDTO.setPositionSalaryId(positionSalaryStep.getPositionSalaryId())
    );

    return employeeResponseDTO;
  }

  @Override
  public String findPositionNameByPositionSalaryStepId(Long stepId) {
    PositionSalaryStepEntity stepEntity = positionSalaryDao.findPositionSalaryById(stepId)
        .orElseThrow(() -> new RuntimeException("해당 PositionSalaryStep이 존재하지 않습니다."));

    PositionEntity position = stepEntity.getPosition();
    return position.getPositionName();
  }

  @Override
  public List<PayStubResponseDTO> findAllPayStubAndYearAndMonth(String employeeId, int year,
      int month) {
    return null;
  }

  @Override
  public List<PayStubResponseDTO> findAllPayStub(String employeeId) {
    List<PayStubEntity> payStubEntities = payStubDao.findAllPayStub(employeeId);

    List<PayStubResponseDTO> payStubResponseDTOS = payStubEntities.stream()
        .map(entity -> {
          PayStubResponseDTO dto = new PayStubResponseDTO();
          dto.setBaseSalary(entity.getBaseSalary());
          dto.setCompanyCode(entity.getCompanyCode());
          dto.setNetPay(entity.getNetPay());
          dto.setOvertimePay(entity.getOvertimePay());
          dto.setPayStubId(entity.getPayStubId());
          dto.setPaymentDate(entity.getPaymentDate());
          dto.setEmployeeId(entity.getEmployeeId());
          dto.setTotalAllowances(entity.getTotalAllowances());
          dto.setTotalPayment(entity.getTotalPayment());
          dto.setTotalDeductions(entity.getTotalDeductions());

          List<AllowanceEntity> allowanceEntities = allowanceDao.findByPayStubId(
              entity.getPayStubId());
          List<AllowanceResponseDTO> allowanceDTOs = allowanceEntities.stream()
              .map(a -> modelMapper.map(a, AllowanceResponseDTO.class))
              .collect(Collectors.toList());

          List<DeductionEntity> deductionEntities = deductionDao.findByPayStubId(
              entity.getPayStubId());
          List<DeductionResponseDTO> deductionDTOs = deductionEntities.stream()
              .map(d -> modelMapper.map(d, DeductionResponseDTO.class))
              .collect(Collectors.toList());

          dto.setAllowances(allowanceDTOs);
          dto.setDeductions(deductionDTOs);

          return dto;
        }).toList();
    return payStubResponseDTOS;

  }

  @Override
  public List<PositionSalaryStepResponseDTO> findPositionSalaryStepByCompanyCode(
      String companyCode) {
    List<PositionSalaryStepEntity> salaryStepEntityList = positionSalaryDao.findAllByCompanyCode(
        companyCode);
    log.info("서비스단: {}", salaryStepEntityList);

    List<PositionSalaryStepResponseDTO> dtoList = salaryStepEntityList.stream()
        .map(entity -> {
          PositionSalaryStepResponseDTO dto = new PositionSalaryStepResponseDTO();
          dto.setPositionSalaryId(entity.getPositionSalaryId());
          dto.setPositionId(entity.getPosition().getPositionId());  // **중요**
          dto.setSalaryStepId(entity.getSalaryStepId());
          dto.setBaseSalary(entity.getBaseSalary());
          dto.setBaseAnnualLeave(entity.getBaseAnnualLeave());
          dto.setOvertimeAllowance(entity.getOvertimeAllowance());
          dto.setPositionAllowance(entity.getPositionAllowance());
          dto.setCompanyCode(entity.getCompanyCode());

          return dto;
        })
        .collect(Collectors.toList());
    return dtoList;
  }

  @Override
  public List<AllowanceResponseDTO> findAllowancesByCompanyCode(String companyCode) {
    List<AllowanceEntity> allowanceEntityList = allowanceDao.findByCompanyCode(companyCode);
    log.info("서비스단" + allowanceEntityList);

    List<AllowanceResponseDTO> allowanceResponseDTOList = allowanceEntityList.stream()
        .map(entity -> modelMapper.map(entity, AllowanceResponseDTO.class))
        .collect(Collectors.toList());
    return allowanceResponseDTOList;
  }

  @Override
  public List<PositionResponseDTO> findPositionsByCompanyCode(String companyCode) {
    List<PositionEntity> positionEntityList = positionDao.findByCompanyCode(companyCode);
    log.info("service단 " + positionEntityList);
    List<PositionResponseDTO> positionResponseDTOList = positionEntityList.stream()
        .map(entity -> modelMapper.map(entity, PositionResponseDTO.class))
        .collect(Collectors.toList());

    log.info("변환된 DTO 리스트 = {}", positionResponseDTOList);
    return positionResponseDTOList;
  }

  /// 급여 계산 로직

  @Transactional
//  @Scheduled(cron = "0 0 0 1 * ?")
  @Scheduled(cron = "0 */5 * * * *")
  public void calculateAndSaveEmployeePayStub() {
    // 1. 모든 직원 리스트 조회 (Feign 호출 또는 내부 서비스)
    List<String> employeeIdList = hrmFeignClient.getEmployeeIds();
    // 2. 직원별로 처리
    for (String employeeId : employeeIdList) {
      EmployeeResponseDTO employee = hrmFeignClient.findEmployee(employeeId);
      if (employee == null || employee.getEmployeeId() == null) {
        throw new RuntimeException("직원 정보를 찾을 수 없습니다.");
      }

      PositionSalaryStepEntity salaryStep = positionSalaryDao.findPositionSalaryById(
              employee.getPositionSalaryId())
          .orElseThrow(() -> new RuntimeException("직원의 직급 정보를 찾을 수 없습니다."));
      // 직급 호봉 아이디를 가져오기
      BigDecimal totalBaseSalary = salaryStep.getBaseSalary()
          .add(salaryStep.getPositionAllowance());
      BigDecimal overtimeHours = attendanceServiceImpl.calculateMonthlyOvertimeHours(employeeId,
          YearMonth.now());
      // 연장근무 수당 * 연장 근무시간
      BigDecimal totalOvertimeAllowance = salaryStep.getOvertimeAllowance().multiply(overtimeHours);

      // 기본급, 연장급, 급여일 저장
      PayStubEntity payStub = PayStubEntity.builder()
          .employeeId(employeeId)
          .companyCode(employee.getCompanyCode())
          .baseSalary(totalBaseSalary)
          .overtimePay(totalOvertimeAllowance)
          .paymentDate(LocalDateTime.now())
          .build();
      payStubDao.save(payStub);

      // 전체 공통수당을 불러오기
      List<AllowanceEntity> employeeAllowances = allowanceDao.findByCompanyCode(
          employee.getCompanyCode());
      List<AllowanceResponseDTO> allowanceResponseList = new ArrayList<>();
      BigDecimal allowanceTotal = BigDecimal.valueOf(0);

      for (AllowanceEntity employeeAllowance : employeeAllowances) {
        AllowanceEntity allowance = AllowanceEntity.builder()
            .allowType(employeeAllowance.getAllowType())
            .allowSalary(employeeAllowance.getAllowSalary())
            .build();
        allowanceTotal = allowanceTotal.add(employeeAllowance.getAllowSalary());
      }

      // ✅ 공제 생성 및 공제 리스트 생성
      createDeductionsForPayStub(payStub);
      List<DeductionEntity> deductionEntities = deductionDao.findByPayStubId(
          payStub.getPayStubId());
      List<DeductionResponseDTO> deductionResponseList = deductionEntities.stream()
          .map(entity -> {
            DeductionResponseDTO dto = modelMapper.map(entity, DeductionResponseDTO.class);
            dto.setPayStubId(entity.getPayStub().getPayStubId());
            return dto;
          })
          .collect(Collectors.toList());

      // ✅ 총합 계산
      BigDecimal totalDeductions = deductionDao.sumByPayStubId(payStub.getPayStubId());
      BigDecimal totalAllowances = allowanceTotal;
      BigDecimal totalPayment = totalBaseSalary.add(totalOvertimeAllowance).add(totalAllowances);
      BigDecimal netPay = totalPayment.subtract(totalDeductions);

      payStub.setTotalAllowances(totalAllowances);
      payStub.setTotalPayment(totalPayment);
      payStub.setTotalDeductions(totalDeductions);
      payStub.setNetPay(netPay);

      payStubDao.save(payStub);

      // ✅ 최종 PayStubResponseDTO 반환 (수당/공제 리스트 포함)
      PayStubResponseDTO responseDTO = modelMapper.map(payStub, PayStubResponseDTO.class);
      responseDTO.setAllowances(allowanceResponseList);
      responseDTO.setDeductions(deductionResponseList);


    }

  }
//    }

  private void createDeductionsForPayStub(PayStubEntity payStub) {
    BigDecimal baseSalary = payStub.getBaseSalary();

    for (DeductionType deductionType : DeductionType.values()) {
      BigDecimal deductionAmount = baseSalary.multiply(BigDecimal.valueOf(deductionType.getRate()));

      DeductionEntity deduction = DeductionEntity.builder()
          .deductionType(deductionType)
          .deductionAmount(deductionAmount)
          .amount(deductionAmount)
          .payStub(payStub)

          .build();

      deductionDao.save(deduction);
    }
  }


  ///  Position 서비스
  // 회사에서 직급 추가
  @Override
  public PositionResponseDTO insertPosition(PositionResponseDTO requestDTO, String companyCode) {
    PositionEntity position = requestDTO.toEntity();
    position.setCompanyCode(companyCode);
    PositionEntity pp = positionDao.savePosition(position);
    return modelMapper.map(pp, PositionResponseDTO.class);
  }

  @Override
  public void updatePosition(PositionResponseDTO requestDTO) {
    PositionEntity position = requestDTO.toEntity();
    positionDao.savePosition(position);
  }

  // 회사에서 직급 삭제
  @Override
  public void deletePosition(PositionResponseDTO requestDTO) {
    PositionEntity position = requestDTO.toEntity();
    positionDao.deletePosition(position);
  }

  @Override
  public PositionResponseDTO findPosition(Long positionId) {
    return modelMapper.map(positionDao.findById(positionId), PositionResponseDTO.class);
  }

  /// positionSalaryStep 서비스
  @Override
  public PositionSalaryStepResponseDTO insertPositionSalaryStep(
      PositionSalaryStepResponseDTO responseDTO) {
    PositionSalaryStepEntity pss = new PositionSalaryStepEntity();

    pss.setBaseAnnualLeave(responseDTO.getBaseAnnualLeave());
    pss.setBaseSalary(responseDTO.getBaseSalary());
    pss.setCompanyCode(responseDTO.getCompanyCode());
    pss.setOvertimeAllowance(responseDTO.getOvertimeAllowance());
    pss.setPositionAllowance(responseDTO.getPositionAllowance());
    pss.setSalaryStepId(responseDTO.getSalaryStepId());

    long salaryPositionId = responseDTO.getPositionId();

    // PositionEntity 꺼내기
    PositionEntity position = positionDao.findById(salaryPositionId)
        .orElseThrow(() -> new RuntimeException("해당하는 Position이 없습니다."));

    pss.setPosition(position);

    // 저장
    PositionSalaryStepEntity saved = positionSalaryDao.savePositionSalaryStep(pss);

    // 리턴할 DTO 수동 변환
    PositionSalaryStepResponseDTO dto = new PositionSalaryStepResponseDTO();
    dto.setPositionSalaryId(saved.getPositionSalaryId());
    dto.setPositionId(saved.getPosition().getPositionId());
    dto.setSalaryStepId(saved.getSalaryStepId());
    dto.setBaseSalary(saved.getBaseSalary());
    dto.setBaseAnnualLeave(saved.getBaseAnnualLeave());
    dto.setOvertimeAllowance(saved.getOvertimeAllowance());
    dto.setPositionAllowance(saved.getPositionAllowance());
    dto.setCompanyCode(saved.getCompanyCode());

    // PositionSalaryStepEntity pss = modelMapper.map(responseDTO, PositionSalaryStepEntity.class);
    // PositionSalaryStepEntity ps = positionSalaryDao.savePositionSalaryStep(pss);
    // PositionSalaryStepResponseDTO dto = modelMapper.map(ps, PositionSalaryStepResponseDTO.class);
    return dto;
  }

  @Override
  public void updatePositionSalaryStep(PositionSalaryStepResponseDTO responseDTO) {
    PositionSalaryStepEntity pss = modelMapper.map(responseDTO, PositionSalaryStepEntity.class);
    positionSalaryDao.savePositionSalaryStep(pss);
  }

  @Override
  public void deletePositionSalaryStep(PositionSalaryStepResponseDTO responseDTO) {
    PositionSalaryStepEntity pss = modelMapper.map(responseDTO, PositionSalaryStepEntity.class);
    positionSalaryDao.deletePosition(pss);
  }

  @Override
  public PositionSalaryStepResponseDTO findPositionSalaryStep(Long positionSalaryId) {
    log.info("서비스단 메서드 positionSalaryStepId:{}", positionSalaryId);
    //return modelMapper.map(positionSalaryDao.findPositionSalaryStepById(positionSalaryStepId), PositionSalaryStepResponseDTO.class);
    Optional<PositionSalaryStepEntity> pss = positionSalaryDao.findPositionSalaryById(
        positionSalaryId);

    return modelMapper.map(pss.get(), PositionSalaryStepResponseDTO.class);
  }


  /// AllowanceResponse 서비스
  @Override
  public AllowanceResponseDTO insertAllowance(AllowanceResponseDTO allowanceResponseDTO) {
    AllowanceEntity allowance = allowanceDao.saveAllowance(allowanceResponseDTO.toEntity());
    return modelMapper.map(allowance, AllowanceResponseDTO.class);
  }

  @Override
  public void updateAllowance(AllowanceResponseDTO responseDTO) {
    AllowanceEntity allowance = modelMapper.map(responseDTO, AllowanceEntity.class);
    allowanceDao.saveAllowance(allowance);
  }

  @Override
  public void deleteAllowance(AllowanceResponseDTO responseDTO) {
    AllowanceEntity allowance = modelMapper.map(responseDTO, AllowanceEntity.class);
    allowanceDao.deleteAllowanceById(allowance.getAllowanceId());
  }

  @Override
  public AllowanceResponseDTO findAllowance(Long allowanceId) {
    return modelMapper.map(allowanceDao.findAllowanceById(allowanceId), AllowanceResponseDTO.class);
  }

  /// Deduction 서비스
  @Override
  public DeductionResponseDTO insertDeduction(DeductionResponseDTO responseDTO) {
    DeductionEntity dd = deductionDao.save(responseDTO.toEntity());
    return modelMapper.map(dd, DeductionResponseDTO.class);
  }

  @Override
  public void updateDeduction(DeductionResponseDTO responseDTO) {
    DeductionEntity deduction = modelMapper.map(responseDTO, DeductionEntity.class);
    deductionDao.save(deduction);
  }

  @Override
  public void deleteDeduction(DeductionResponseDTO responseDTO) {
    deductionDao.deleteById(responseDTO.getDeductionId());
  }

  @Override
  public DeductionResponseDTO findDeduction(Long deductionId) {
    return modelMapper.map(deductionDao.fetchById(deductionId), DeductionResponseDTO.class);
  }

  /// PayStub 서비스
  @Override
  public PayStubResponseDTO insertPayStub(PayStubResponseDTO responseDTO) {
    PayStubEntity pp = modelMapper.map(responseDTO, PayStubEntity.class);
    PayStubEntity ps = payStubDao.save(pp);
    return modelMapper.map(ps, PayStubResponseDTO.class);
  }

  @Override
  public void updateDeduction(PayStubResponseDTO responseDTO) {
    PayStubEntity pp = modelMapper.map(responseDTO, PayStubEntity.class);
    payStubDao.save(pp);
  }

  @Override
  public void deleteDeduction(PayStubResponseDTO responseDTO) {
    payStubDao.delete(modelMapper.map(responseDTO, PayStubEntity.class));

    //payStubDao.delete(responseDTO.toEntity());

  }

  @Override
  public PayStubResponseDTO findPayStubByEmployeeId(String employeeId) {
    PayStubEntity payStub = payStubDao.findPayStubByeEmployeeId(employeeId);
    return modelMapper.map(payStub, PayStubResponseDTO.class);
  }


  @Override
  public List<AllowanceResponseDTO> findAllowancesByPayStubId(Long payStubId) {
    List<AllowanceEntity> allowances = allowanceDao.findByPayStubId(payStubId);
    return allowances.stream()
        .map(entity -> modelMapper.map(entity, AllowanceResponseDTO.class))
        .collect(Collectors.toList());
  }

  @Override
  public List<DeductionResponseDTO> findDeductionsByPayStubId(Long payStubId) {
    List<DeductionEntity> deductions = deductionDao.findByPayStubId(payStubId);
    return deductions.stream()
        .map(entity -> modelMapper.map(entity, DeductionResponseDTO.class))
        .collect(Collectors.toList());
  }

  @Override
  public EmployeeAllowDTO insertEmployeeAllow(EmployeeAllowDTO responseDTO) {
    EmployeeAllowEntity ea = modelMapper.map(responseDTO, EmployeeAllowEntity.class);
    employeeAllowDao.save(ea);
    return modelMapper.map(ea, EmployeeAllowDTO.class);
  }

  @Override
  public void updateEmployeeAllow(EmployeeAllowDTO responseDTO) {
    EmployeeAllowEntity ea = modelMapper.map(responseDTO, EmployeeAllowEntity.class);
    employeeAllowDao.update(ea);
  }

  @Override
  public void deleteEmployeeAllow(EmployeeAllowDTO responseDTO) {
    EmployeeAllowEntity ea = modelMapper.map(responseDTO, EmployeeAllowEntity.class);
    employeeAllowDao.delete(ea);
  }


  @Override
  public EmployeeAllowDTO findEmployeeAllowByEmployeeId(Long employeeId) {
    Optional<EmployeeAllowEntity> ea = employeeAllowDao.selectByEmployeeAllowId(employeeId);
    return modelMapper.map(ea.get(), EmployeeAllowDTO.class);
  }
/// EmployeeAllowService
//    @Override
//    public EmployeeAllowDTO insertEmployeeAllow(EmployeeAllowDTO responseDTO) {
//       EmployeeAllowEntity ea = modelMapper.map(responseDTO, EmployeeAllowEntity.class);
//       employeeAllowDao.save(ea);
//       return modelMapper.map(ea, EmployeeAllowDTO.class);
//    }
//
//    @Override
//    public void updateEmployeeAllow(EmployeeAllowDTO responseDTO) {
//        EmployeeAllowEntity ea = modelMapper.map(responseDTO, EmployeeAllowEntity.class);
//        employeeAllowDao.update(ea);
//    }
//
//    @Override
//    public void deleteEmployeeAllow(EmployeeAllowDTO responseDTO) {
//        EmployeeAllowEntity ea = modelMapper.map(responseDTO, EmployeeAllowEntity.class);
//        employeeAllowDao.delete(ea);
//    }
//
//
//    @Override
//    public EmployeeAllowDTO findEmployeeAllowByEmployeeId(Long employeeId) {
//        Optional<EmployeeAllowEntity> ea = employeeAllowDao.selectByEmployeeAllowId(employeeId);
//        return modelMapper.map(ea.get(), EmployeeAllowDTO.class);
//    }

}
