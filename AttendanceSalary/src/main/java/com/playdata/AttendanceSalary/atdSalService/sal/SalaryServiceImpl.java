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
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional
  public PayStubResponseDTO calculateAndSaveEmployeePayStub(String employeeId) {

    // ✅ Feign으로 직원 정보 가져오기
    EmployeeResponseDTO employee = hrmFeignClient.findEmployee(employeeId);

    if (employee == null || employee.getEmployeeId() == null) {
      throw new RuntimeException("직원 정보를 찾을 수 없습니다.");
    }

    PositionSalaryStepEntity salaryStep = positionSalaryDao.findPositionSalaryById(employee.getPositionSalaryId())
            .orElseThrow(() -> new RuntimeException("직원의 직급 정보를 찾을 수 없습니다."));

    BigDecimal totalBaseSalary = salaryStep.getBaseSalary().add(salaryStep.getPositionAllowance());

    BigDecimal overtimeHours = attendanceServiceImpl.calculateMonthlyOvertimeHours(employeeId, YearMonth.now());
    BigDecimal totalOvertimeAllowance = salaryStep.getOvertimeAllowance().multiply(overtimeHours);

    PayStubEntity payStub = PayStubEntity.builder()
            .employeeId(employeeId)
            .companyCode(employee.getCompanyCode())
            .baseSalary(totalBaseSalary)
            .overtimePay(totalOvertimeAllowance)
            .paymentDate(LocalDateTime.now())
            .build();

    payStubDao.save(payStub);

    List<EmployeeAllowEntity> employeeAllowances = employeeAllowDao.findByEmployeeId(employeeId);

    for (EmployeeAllowEntity employeeAllowance : employeeAllowances) {
      AllowanceEntity allowance = AllowanceEntity.builder()
              .companyCode(employee.getCompanyCode())
              .allowType(employeeAllowance.getAllowanceType())
              .allowSalary(employeeAllowance.getAmount())
              .payStub(payStub)
              .build();

      allowanceDao.saveAllowance(allowance);
    }

    BigDecimal nationalPension = totalBaseSalary.multiply(BigDecimal.valueOf(0.045));
    BigDecimal healthInsurance = totalBaseSalary.multiply(BigDecimal.valueOf(0.035));
    BigDecimal employmentInsurance = totalBaseSalary.multiply(BigDecimal.valueOf(0.009));

    deductionDao.save(new DeductionEntity(null, DeductionType.NATIONAL_PENSION, nationalPension, payStub));
    deductionDao.save(new DeductionEntity(null, DeductionType.HEALTH_INSURANCE, healthInsurance, payStub));
    deductionDao.save(new DeductionEntity(null, DeductionType.EMPLOYMENT_INSURANCE, employmentInsurance, payStub));

    BigDecimal totalDeductions = deductionDao.sumByPayStubId(payStub.getPayStubId());

    BigDecimal totalAllowances = employeeAllowances.stream()
            .map(EmployeeAllowEntity::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalPayment = totalBaseSalary.add(totalOvertimeAllowance).add(totalAllowances);
    BigDecimal netPay = totalPayment.subtract(totalDeductions);

    payStub.setTotalAllowances(totalAllowances);
    payStub.setTotalPayment(totalPayment);
    payStub.setTotalDeductions(totalDeductions);
    payStub.setNetPay(netPay);

    payStubDao.save(payStub);

    return modelMapper.map(payStub, PayStubResponseDTO.class);
  }

  private void createDeductionsForPayStub(PayStubEntity payStub) {
    BigDecimal baseSalary = payStub.getBaseSalary();

    /// 국민연금: 4.5%
    BigDecimal nationalPension = baseSalary.multiply(BigDecimal.valueOf(0.045));

    /// 건강보험: 3.5%
    BigDecimal healthInsurance = baseSalary.multiply(BigDecimal.valueOf(0.035));

    /// 고용보험: 0.9%
    BigDecimal employmentInsurance = baseSalary.multiply(BigDecimal.valueOf(0.009));

    deductionDao.save(new DeductionEntity(null, DeductionType.NATIONAL_PENSION, nationalPension, payStub));
    deductionDao.save(new DeductionEntity(null, DeductionType.HEALTH_INSURANCE, healthInsurance, payStub));
    deductionDao.save(new DeductionEntity(null, DeductionType.EMPLOYMENT_INSURANCE, employmentInsurance, payStub));
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
