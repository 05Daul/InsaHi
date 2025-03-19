package com.playdata.AttendanceSalary.atdSalService.sal;


import com.playdata.AttendanceSalary.atdClient.HrmFeignClient;
import com.playdata.AttendanceSalary.atdClient.hrmDTO.EmployeeResponseDTO;
import com.playdata.AttendanceSalary.atdSalDao.sal.*;
import com.playdata.AttendanceSalary.atdSalDto.sal.*;
import com.playdata.AttendanceSalary.atdSalEntity.sal.*;
import com.playdata.AttendanceSalary.atdSalService.atd.AttendanceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
  //    private final SalaryDao salaryDao;
  private final HrmFeignClient hrmFeignClient;
  private final AttendanceServiceImpl attendanceServiceImpl; // dao에서 로직 구성 x => service로


  /// 급여 계산 로직
  @Override
  public PayStubResponseDTO calculateAndSaveEmployeePayStub(String employeeId) {

    // 1. 직원 정보 조회
    EmployeeResponseDTO employee = hrmFeignClient.findEmployee(employeeId);
    log.info("employeeId:{}", employee.getEmployeeId());

    if (employee.getEmployeeId() == null) {
      throw new RuntimeException("직원 정보를 찾을 수 없습니다.");
    }

    // 2. 직급 및 호봉 정보 조회
    if (employee.getPositionSalaryId() == null) {
      throw new RuntimeException("직원의 직급 정보를 찾을 수 없습니다.");
    }

    PositionSalaryStepResponseDTO salaryStep = findPositionSalaryStep(
        employee.getPositionSalaryId());
    log.info("employeePositionSalaryId:{}", salaryStep);
    log.info("BaseSalary:{}", salaryStep.getBaseSalary());

    // 2.1 기본급
    BigDecimal baseSalary = salaryStep.getBaseSalary();
    // 2.2 직급수당
    BigDecimal positionAllowance = salaryStep.getPositionAllowance();

    // ✅ 기준급 = 기본급 + 직급수당
    BigDecimal totalBaseSalary = baseSalary.add(positionAllowance);
    log.info("기준급 (기본급 + 직급수당): {}", totalBaseSalary);

    // 2.3 연장근로수당 (시간 * 시간당 수당)
    BigDecimal hourlyOvertimeAllowance = salaryStep.getOvertimeAllowance();
    BigDecimal overtimeHours = attendanceServiceImpl.calculateMonthlyOvertimeHours(employeeId,
        YearMonth.now());
    BigDecimal totalOvertimeAllowance = hourlyOvertimeAllowance.multiply(overtimeHours);

    log.info("연장근로 수당 = {}", totalOvertimeAllowance);

    // 3. PayStubEntity 초기 생성
    PayStubEntity payStubEntity = PayStubEntity.builder()
        .employeeId(employeeId)
        .companyCode(employee.getCompanyCode())
        .baseSalary(totalBaseSalary) // 기준급 저장
        .paymentDate(LocalDateTime.now())
        .build();

    PayStubEntity savedStub = payStubDao.save(payStubEntity);

    // 4. 직원별 수당 조회 및 계산
    List<EmployeeAllowEntity> employeeAllowances = employeeAllowDao.findByEmployeeId(employeeId);

    BigDecimal totalTaxFreeAllowances = BigDecimal.ZERO;
    BigDecimal totalTaxableAllowances = BigDecimal.ZERO;

    for (EmployeeAllowEntity employeeAllowance : employeeAllowances) {

      AllowanceType type = employeeAllowance.getAllowanceType();
      BigDecimal amount = employeeAllowance.getAmount();

      boolean isTaxExempt = type.isTaxExemption();
      BigDecimal exemptionLimit = type.getTaxExemptionLimit();

      if (isTaxExempt) {
        BigDecimal taxFreeAmount = amount.min(exemptionLimit);
        BigDecimal taxableAmount = amount.subtract(exemptionLimit).max(BigDecimal.ZERO);

        totalTaxFreeAllowances = totalTaxFreeAllowances.add(taxFreeAmount);
        totalTaxableAllowances = totalTaxableAllowances.add(taxableAmount);

        log.info("[비과세 수당] {} - 비과세 금액: {}, 과세 금액: {}",
            type.getDisplayName(), taxFreeAmount, taxableAmount);

      } else {
        totalTaxableAllowances = totalTaxableAllowances.add(amount);

        log.info("[과세 수당] {} - 과세 금액: {}", type.getDisplayName(), amount);
      }
    }

    // 5. 공제 항목 계산 및 저장
    List<DeductionEntity> deductionEntities = calculateAndSaveDeductions(totalBaseSalary,
        savedStub);

    BigDecimal totalDeductions = deductionEntities.stream()
        .map(DeductionEntity::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    // 6. 총 지급액 계산 (기준급 + 연장근로수당 + 수당)
    BigDecimal totalPayment = totalBaseSalary
        .add(totalOvertimeAllowance)
        .add(totalTaxableAllowances)
        .add(totalTaxFreeAllowances);

    // 7. 실 지급액 계산
    BigDecimal netPay = totalPayment.subtract(totalDeductions);

    // 8. PayStubEntity 최종 업데이트 및 저장
    savedStub.setBaseSalary(totalBaseSalary);
    savedStub.setTotalAllowances(totalTaxableAllowances.add(totalTaxFreeAllowances));
    savedStub.setTotalTaxFreeAllowances(totalTaxFreeAllowances);
    savedStub.setOvertimePay(totalOvertimeAllowance);
    savedStub.setTotalDeductions(totalDeductions);
    savedStub.setTotalPayment(totalPayment);
    savedStub.setNetPay(netPay);

    PayStubEntity finalStub = payStubDao.save(savedStub);

    log.info("급여 계산 완료 - 총 지급액 : {}", totalPayment);
    log.info("급여 계산 완료 - 공제 후 실 지급액 : {}", netPay);

    return modelMapper.map(finalStub, PayStubResponseDTO.class);
  }
//
//    @Override
//    public PayStubResponseDTO calculateAndSaveEmployeePayStub(String employeeId) {
//
//        // 1. employee 직원을 ID를 통해 정보 조회
//
//        EmployeeResponseDTO employee = hrmFeignClient.findEmployee(employeeId);
//        log.info("employeeId:{}", employee.getEmployeeId());
//
//        if (employee.getEmployeeId() == null) {
//            throw new RuntimeException("직원 정보를 찾을 수 없습니다.");
//        }
//
//        // 2. employee직원의 직급 정보 조회
//        if (employee.getPositionSalaryId() == null) {
//            throw new RuntimeException("직원의 직급 정보를 찾을 수 없습니다.");
//        }
//
//
//        ///  직급 호봉(직급 아이디, 호봉, 기본급, 직급당 수당, 연장 수당, 기본 연차, 회사 코드를 부여)
//        ///    private Long positionSalaryId;
//        ///    private PositionEntity positionId; // 직급 아이디
//        ///     private Long salaryStepId; // 호봉 ⚠️호봉 로직 수정필요: 입사일 기준 호봉이 늘어나게 필요
//        ///     private BigDecimal baseSalary; //기본금 ️☑️
//        ///     private BigDecimal positionAllowance; //직급 수당 ☑️
//        ///     private BigDecimal overtimeAllowance; // 연장수당 ☑️
//        ///     private int baseAnnualLeave; //기본 연차   여기서 사용 ❌
//        ///     private String companyCode;  여기서  사용안함❌
//
//
//        PositionSalaryStepResponseDTO salaryStep = findPositionSalaryStep(employee.getPositionSalaryId());
//        log.info("employeePositionSalaryId:{}", salaryStep);
//        log.info("BaseSalary:{}", salaryStep.getBaseSalary());
//
//        // 2.1 기본급
//        BigDecimal baseSalary = salaryStep.getBaseSalary();
//        // 2.2 직급수당 (과세 대상)
//        BigDecimal positionAllowance = salaryStep.getPositionAllowance();
//        // 2.3 연장 급 (과세 대상)
//        //ㄱ. 시간당 연장 수당
//        BigDecimal hourlyOvertimeAllowance = salaryStep.getOvertimeAllowance();
//        //ㄴ. 이번달 연장근로 시간
//        BigDecimal overtimeHours = attendanceServiceImpl.calculateMonthlyOvertimeHours(employeeId, YearMonth.now());
//        //ㄱ * ㄴ = 이번 달 연장 수당
//        BigDecimal totalOvertimeAllowance = hourlyOvertimeAllowance.multiply(overtimeHours);
//
//
//        log.info("overtimeAllowance = {}", totalOvertimeAllowance);
//        log.info("baseSalary = {}", baseSalary);
//        log.info("positionAllowance = {}", positionAllowance);
//
//        // 3. 급여명세서 초기 생성 및 저장 (직급수당과 연장수당 포함)
//        ///   private LocalDateTime paymentDate☑️ 자동생성
//        /// 지급일 ☑️
//        ///   private BigDecimal baseSalary; 기본급☑️
//        ///    private BigDecimal totalAllowances; 총 수당
//        ///   private BigDecimal overtimePay; 연장 수당
//        ///   private BigDecimal totalPayment; 총 지불
//        ///    private BigDecimal totalDeductions; 총 공제
//        ///   private BigDecimal netPay; 실 수령액
//        ///   private String companyCode; 회사코드☑️
//        ///   private String employeeId; 급여자☑️
//        PayStubEntity payStubEntity = PayStubEntity.builder()
//                .employeeId(employeeId)
//                .companyCode(employee.getCompanyCode())
//                .baseSalary(baseSalary)
//                .paymentDate(LocalDateTime.now()) // 지급일 현재
//                .build();
//        PayStubEntity savedStub = payStubDao.save(payStubEntity);
//
//        // 4. 수당 조회 및 합산 (비과세/과세 구분)
//        List<AllowanceEntity> allowances = findAllowancesByPayStubId(savedStub.getPayStubId())
//                .stream()
//                .map(dto -> modelMapper.map(dto, AllowanceEntity.class))
//                .toList();
//
//        BigDecimal totalTaxFreeAllowances = BigDecimal.ZERO; // 비과세 수당 총액
//        BigDecimal totalTaxableAllowances = BigDecimal.ZERO; // 과세 수당 총액
//
//        for (AllowanceEntity allowance : allowances) {
//
//            // Enum 값 가져오기
//            AllowanceType type = allowance.getAllowType();
//
//            // 비과세 여부와 비과세 한도
//            boolean isTaxExempt = type.isTaxExemption();
//            BigDecimal exemptionLimit = type.getTaxExemptionLimit();
//            BigDecimal allowanceAmount = allowance.getAllowSalary();
//
//            // 비과세 여부 판단
//            if (isTaxExempt) {
//                // 비과세 한도 적용
//                BigDecimal taxFreeAmount = allowanceAmount.min(exemptionLimit);
//                BigDecimal taxableAmount = allowanceAmount.subtract(exemptionLimit).max(BigDecimal.ZERO);
//
//                totalTaxFreeAllowances = totalTaxFreeAllowances.add(taxFreeAmount);
//                totalTaxableAllowances = totalTaxableAllowances.add(taxableAmount);
//
//                log.info("[비과세 수당] {} - 비과세 금액: {}, 과세 금액: {}",
//                        type.getDisplayName(), taxFreeAmount, taxableAmount);
//
//            } else {
//                // 전액 과세 처리
//                totalTaxableAllowances = totalTaxableAllowances.add(allowanceAmount);
//
//                log.info("[과세 수당] {} - 과세 금액: {}", type.getDisplayName(), allowanceAmount);
//            }
//        }
//        // 5. 공제 항목 계산 및 저장
//        List<DeductionEntity> deductionEntities = calculateAndSaveDeductions(baseSalary, savedStub);
//
//        // 6. 총 공제액 계산
//        BigDecimal totalDeductions = deductionEntities.stream()
//                .map(DeductionEntity::getAmount)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        // 7. 총 지급액 계산 (과세 지급액 + 비과세 지급액 포함)
//        BigDecimal totalPayment = baseSalary
//                .add(positionAllowance)
//                .add(totalOvertimeAllowance)
//                .add(totalTaxableAllowances)
//                .add(totalTaxFreeAllowances);
//
//        // 8. 실 지급액 (총 지급액 - 공제액)
//        BigDecimal netPay = totalPayment.subtract(totalDeductions);
//
//        // 9. PayStubEntity 최종 업데이트 및 저장
//        savedStub.setTotalAllowances(totalTaxableAllowances.add(totalTaxFreeAllowances)); // 전체 수당 합계
//        savedStub.setTotalTaxFreeAllowances(totalTaxFreeAllowances); // 비과세 수당 합계 (선택 필드)
//        savedStub.setTotalDeductions(totalDeductions);
//
//        savedStub.setTotalDeductions(totalDeductions);
//        savedStub.setTotalPayment(totalPayment);
//        savedStub.setNetPay(netPay);
//
//        PayStubEntity finalStub = payStubDao.save(savedStub);
//
//        log.info("급여 계산 완료 - 총 지급액 : {}", totalPayment);
//        log.info("급여 계산 완료 - 공제 후 실 지급액 : {}", netPay);
//
//        return modelMapper.map(finalStub, PayStubResponseDTO.class);
//    }

  private List<DeductionEntity> calculateAndSaveDeductions(BigDecimal baseSalary,
      PayStubEntity payStubEntity) {

    List<DeductionEntity> deductions = Arrays.stream(DeductionType.values())
        .map(type -> {

          // 공제액 계산: 기본급 * 공제율
          BigDecimal deductionAmount = baseSalary.multiply(BigDecimal.valueOf(type.getRate()));
          DeductionEntity deductionEntity = new DeductionEntity();
          deductionEntity.setDeductionType(type);
          deductionEntity.setAmount(deductionAmount);
          deductionEntity.setPayStub(payStubEntity);
          return deductionEntity;
        })
        .collect(Collectors.toList());
    deductionDao.saveAll(deductions);

    return deductions;
  }

  ///  Position 서비스
  // 회사에서 직급 추가
  @Override
  public PositionResponseDTO insertPosition(PositionResponseDTO requestDTO, String CompanyCode) {
    PositionEntity position = requestDTO.toEntity();
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
    PositionSalaryStepEntity pss = modelMapper.map(responseDTO, PositionSalaryStepEntity.class);
    PositionSalaryStepEntity ps = positionSalaryDao.savePositionSalaryStep(pss);
    PositionSalaryStepResponseDTO dto = modelMapper.map(ps, PositionSalaryStepResponseDTO.class);
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
    log.info(pss.toString());
    return modelMapper.map(pss.get(), PositionSalaryStepResponseDTO.class);
  }

  /// AllowanceResponse 서비스
  @Override
  public AllowanceResponseDTO insertAllowance(AllowanceResponseDTO allowanceResponseDTO) {
    AllowanceEntity allowance = allowanceDao.saveAllowance(allowanceResponseDTO.toEntity());
    return modelMapper.map(allowance, AllowanceResponseDTO.class);
  }

  @Override
  public List<AllowanceEntity> findByAllowance_CompanyCode(String CompanyCode) {
    return List.of();
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
    PayStubEntity pp = modelMapper.map(responseDTO, PayStubEntity.class);
    payStubDao.delete(responseDTO.toEntity());
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
//
//  @Override
//  public EmployeeAllowDTO insertEmployeeAllow(EmployeeAllowDTO responseDTO) {
//    EmployeeAllowEntity ea = modelMapper.map(responseDTO, EmployeeAllowEntity.class);
//    employeeAllowDao.save(ea);
//    return modelMapper.map(ea, EmployeeAllowDTO.class);
//  }
//
//  @Override
//  public void updateEmployeeAllow(EmployeeAllowDTO responseDTO) {
//    EmployeeAllowEntity ea = modelMapper.map(responseDTO, EmployeeAllowEntity.class);
//    employeeAllowDao.update(ea);
//  }
//
//  @Override
//  public void deleteEmployeeAllow(EmployeeAllowDTO responseDTO) {
//    EmployeeAllowEntity ea = modelMapper.map(responseDTO, EmployeeAllowEntity.class);
//    employeeAllowDao.delete(ea);
//  }
//
//
//  @Override
//  public EmployeeAllowDTO findEmployeeAllowByEmployeeId(Long employeeId) {
//    Optional<EmployeeAllowEntity> ea = employeeAllowDao.selectByEmployeeAllowId(employeeId);
//    return modelMapper.map(ea.get(), EmployeeAllowDTO.class);
//  }
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
