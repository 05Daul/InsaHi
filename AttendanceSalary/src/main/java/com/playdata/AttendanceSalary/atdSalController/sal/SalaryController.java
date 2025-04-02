package com.playdata.AttendanceSalary.atdSalController.sal;

import com.playdata.AttendanceSalary.atdSalDto.sal.AllowanceResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.DeductionResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.PayStubResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.PositionResponseDTO;
import com.playdata.AttendanceSalary.atdSalDto.sal.PositionSalaryStepResponseDTO;
import com.playdata.AttendanceSalary.atdSalService.sal.SalaryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/atdsal")
@RequiredArgsConstructor
public class SalaryController {

  private final SalaryService salaryService;

  @PutMapping
  public PositionSalaryStepResponseDTO updatePositionSalary(Long positionSalaryId) {
    return salaryService.findPositionSalaryStep(positionSalaryId);
  }

  @GetMapping("/payStub-findall")
  public List<PayStubResponseDTO> payStubFindAll(@RequestParam("employeeId") String employeeId) {
    return salaryService.findAllPayStub(employeeId);
  }

  //    @PostMapping("/payStub")
//    public PayStubResponseDTO payStub(@RequestParam("employeeId") String employeeId) {
//        System.out.println("employeeId = " + employeeId);
//        return salaryService.calculateAndSaveEmployeePayStub(employeeId);
//    }
  @GetMapping("/salary-step-list")
  public List<PositionSalaryStepResponseDTO> positionSalaryStepList(
      @RequestParam("companyCode") String companyCode) {
    return salaryService.findPositionSalaryStepByCompanyCode(companyCode);
  }

  /// 1. Position 서비스
  @PostMapping("/position-insert")
  public PositionResponseDTO positionInsert(@RequestBody PositionResponseDTO responseDTO) {
    String companyCode = responseDTO.getCompanyCode();  // 이미 바디에서 가져올 수 있음
    return salaryService.insertPosition(responseDTO, companyCode);

  }

  @PutMapping("/position-update")
  public void positionupdate(@RequestBody PositionResponseDTO responseDTO) {
    salaryService.updatePosition(responseDTO);
  }

  @DeleteMapping("/position-delete")
  public void positionDelete(@RequestBody PositionResponseDTO responseDTO) {
    salaryService.deletePosition(responseDTO);

  }

  @GetMapping("/position-find")
  public PositionResponseDTO positionFind(@RequestParam("positionId") Long positionId) {
    return salaryService.findPosition(positionId);
  }

  @GetMapping("/position-list")
  public List<PositionResponseDTO> positionList(@RequestParam("companyCode") String companyCode) {
    log.info("companyCode = " + companyCode);
    List<PositionResponseDTO> positions = salaryService.findPositionsByCompanyCode(companyCode);

    log.info("positions = {}", positions);  // 리스트 로그 확인

    return positions;
  }

  /// 2. PositionSalaryStep 서비스
  @PostMapping("/step-insert")
  public PositionSalaryStepResponseDTO positionInsert(
      @RequestBody PositionSalaryStepResponseDTO responseDTO) {
    return salaryService.insertPositionSalaryStep(responseDTO);
  }

  @GetMapping("/position-name")
  public String getPositionName(@RequestParam("stepId") Long stepId) {
    return salaryService.findPositionNameByPositionSalaryStepId(stepId);
  }

  @PutMapping("/step-update")
  public void positionupdate(@RequestBody PositionSalaryStepResponseDTO responseDTO) {
    salaryService.updatePositionSalaryStep(responseDTO);
  }

  @DeleteMapping("/step-delete")
  public void positionDelete(@RequestBody PositionSalaryStepResponseDTO responseDTO) {
    salaryService.deletePositionSalaryStep(responseDTO);
  }

  @GetMapping("/step-find")
  public PositionSalaryStepResponseDTO positionSalaryStepFind(@RequestParam("id") Long id) {
    return salaryService.findPositionSalaryStep(id);

  }

  /// AllowanceResponseDTO
  @PostMapping("/allowance-insert")
  public AllowanceResponseDTO insertAllowance(@RequestBody AllowanceResponseDTO responseDTO) {
    return salaryService.insertAllowance(responseDTO);
  }

  @PutMapping("/allowance-update")
  public void updateAllowance(@RequestBody AllowanceResponseDTO responseDTO) {
    salaryService.updateAllowance(responseDTO);
  }

  @DeleteMapping("/allowance-delete")
  public void deleteAllowance(@RequestBody AllowanceResponseDTO responseDTO) {
    salaryService.deleteAllowance(responseDTO);
  }

  @GetMapping("/allowance-find")
  public AllowanceResponseDTO findAllowance(@RequestParam("allowanceId") Long allowanceId) {
    return salaryService.findAllowance(allowanceId);
  }

  @GetMapping("allowance-list")
  public List<AllowanceResponseDTO> findAllowanceList(
      @RequestParam("companyCode") String companyCode) {
    return salaryService.findAllowancesByCompanyCode(companyCode);
  }

  /// Deduction 서비스
  @PostMapping("deduction-insert")
  public DeductionResponseDTO insertDeduction(@RequestBody() DeductionResponseDTO responseDTO) {
    return salaryService.insertDeduction(responseDTO);
  }

  @PutMapping("deduction-update")
  public void updateDeduction(@RequestBody DeductionResponseDTO responseDTO) {
    salaryService.updateDeduction(responseDTO);
  }

  @DeleteMapping("deduction-delete")
  public void deleteDeduction(@RequestBody DeductionResponseDTO responseDTO) {
    salaryService.deleteDeduction(responseDTO);
  }

  @GetMapping("deduction-find")
  public DeductionResponseDTO findDeduction(@RequestParam("deductionId") Long deductionId) {
    return salaryService.findDeduction(deductionId);
  }


  /// PayStub 서비스
  @PostMapping("/paystub-insert")
  public PayStubResponseDTO insertPayStub(@RequestBody PayStubResponseDTO responseDTO) {
    return salaryService.insertPayStub(responseDTO);
  }

  @PutMapping("/paystub-update")
  public void updateDeduction(@RequestBody PayStubResponseDTO responseDTO) {
    salaryService.updateDeduction(responseDTO);
  }

  @DeleteMapping("/paystub-delete")
  public void deleteDeduction(@RequestBody PayStubResponseDTO responseDTO) {
    salaryService.deleteDeduction(responseDTO);

  }

//    @GetMapping("/paystub-find")
//    public PayStubResponseDTO findPayStub(Long payStubId) {
//        return salaryService.findPayStub(payStubId);
//    }

  /// Salary 서비스
//    @PostMapping("insert")
//    public SalaryResponseDTO insertSalary(@RequestBody SalaryResponseDTO responseDTO) {
//        return salaryService.insertSalary(responseDTO);
//    }
//
//    @PutMapping("update")
//    public void updateSalary(@RequestBody SalaryResponseDTO responseDTO) {
//        salaryService.updateSalary(responseDTO);
//    }
//
//    @DeleteMapping("delete")
//    public void deleteSalary(@RequestBody SalaryResponseDTO responseDTO) {
//        salaryService.deleteSalary(responseDTO);
//    }
//
//    @GetMapping("find")
//    public SalaryResponseDTO findSalary(@RequestParam Long salaryId) {
//        return salaryService.findSalary(salaryId);
//    }

}
