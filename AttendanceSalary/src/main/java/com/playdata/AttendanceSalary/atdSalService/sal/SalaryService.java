package com.playdata.AttendanceSalary.atdSalService.sal;

import com.playdata.AttendanceSalary.atdSalDto.sal.AllowanceResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.DeductionResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.EmployeeAllowDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.PayStubResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.PositionResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.PositionSalaryStepResponseDTO;
import java.util.List;


public interface SalaryService {

  List<PayStubResponseDTO> findAllPayStub(String employeeId);

  List<PayStubResponseDTO> findAllPayStubAndYearAndMonth(String employeeId, int year, int month);

  /// 급여계산 로직
  public void calculateAndSaveEmployeePayStub();

  List<PositionSalaryStepResponseDTO> findPositionSalaryStepByCompanyCode(String CompanyCode);

  /// Position  서비스
  // 회사별 직급 생성
  PositionResponseDTO insertPosition(PositionResponseDTO requestDTO, String companyCode);

  // 직급 수정
  void updatePosition(PositionResponseDTO requestDTO);

  // 직급 삭제
  void deletePosition(PositionResponseDTO requestDTO);

  PositionResponseDTO findPosition(Long positionId);


  /// PositionSalaryStep 서비스
  PositionSalaryStepResponseDTO insertPositionSalaryStep(PositionSalaryStepResponseDTO responseDTO);

  void updatePositionSalaryStep(PositionSalaryStepResponseDTO responseDTO);

  void deletePositionSalaryStep(PositionSalaryStepResponseDTO responseDTO);

  PositionSalaryStepResponseDTO findPositionSalaryStep(Long positionSalaryId);

  /// Allowance 서비스
  AllowanceResponseDTO insertAllowance(AllowanceResponseDTO responseDTO);

  void updateAllowance(AllowanceResponseDTO responseDTO);

  void deleteAllowance(AllowanceResponseDTO responseDTO);

  AllowanceResponseDTO findAllowance(Long allowanceId);

  List<AllowanceResponseDTO> findAllowancesByCompanyCode(String CompanyCode);


  /// Deduction 서비스
  DeductionResponseDTO insertDeduction(DeductionResponseDTO responseDTO);

  void updateDeduction(DeductionResponseDTO responseDTO);

  void deleteDeduction(DeductionResponseDTO responseDTO);

  DeductionResponseDTO findDeduction(Long deductionId);

  List<AllowanceResponseDTO> findAllowancesByPayStubId(Long payStubId);

  List<DeductionResponseDTO> findDeductionsByPayStubId(Long payStubId);

  /// paystub 서비스
  PayStubResponseDTO insertPayStub(PayStubResponseDTO responseDTO);

  void updateDeduction(PayStubResponseDTO responseDTO);

  void deleteDeduction(PayStubResponseDTO responseDTO);


  PayStubResponseDTO findPayStubByEmployeeId(String employeeId);

  EmployeeAllowDTO insertEmployeeAllow(EmployeeAllowDTO responseDTO);

  void updateEmployeeAllow(EmployeeAllowDTO responseDTO);

  void deleteEmployeeAllow(EmployeeAllowDTO responseDTO);

  EmployeeAllowDTO findEmployeeAllowByEmployeeId(Long employeeId);

  List<PositionResponseDTO> findPositionsByCompanyCode(String companyCode);

  String findPositionNameByPositionSalaryStepId(Long stepId);
}
//
//    /// Salary 서비스
//    SalaryResponseDTO insertSalary(SalaryResponseDTO responseDTO);
//
//    void updateSalary(SalaryResponseDTO responseDTO);
//
//    void deleteSalary(SalaryResponseDTO responseDTO);
//
//    SalaryResponseDTO findSalary(Long salaryId);
//
//
//}
