package com.playdata.AttendanceSalary.atdSalController.sal;

import com.playdata.AttendanceSalary.atdSalDto.sal.*;
import com.playdata.AttendanceSalary.atdSalService.sal.SalaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/atdsal")
@RequiredArgsConstructor
public class SalaryController {
    private final SalaryService salaryService;

    @PostMapping("payStub")
    public PayStubResponseDTO payStub(@RequestParam("employeeId") String employeeId) {
        System.out.println("employeeId = " + employeeId);
        return salaryService.calculateAndSaveEmployeePayStub(employeeId);


    }

    /// 1. Position 서비스
    @PostMapping("/position-insert")
    public PositionResponseDTO positionInsert(@RequestBody PositionResponseDTO responseDTO,
                                              @RequestParam String companyCode) {
        return salaryService.insertPosition(responseDTO, companyCode);

    }

    @PutMapping("/positon-update")
    public void positionupdate(@RequestBody PositionResponseDTO responseDTO) {
        salaryService.updatePosition(responseDTO);
    }

    @DeleteMapping("/positon-delete")
    public void positionDelete(@RequestBody PositionResponseDTO responseDTO) {
        salaryService.deletePosition(responseDTO);

    }

    @GetMapping("/positon-find")
    public PositionResponseDTO positionFind(@RequestParam Long positionId) {
        return salaryService.findPosition(positionId);
    }

    /// 2. PositionSalaryStep 서비스
    @PostMapping("step-insert")
    public PositionSalaryStepResponseDTO positionInsert(@RequestBody PositionSalaryStepResponseDTO responseDTO) {
        return salaryService.insertPositionSalaryStep(responseDTO);
    }

    @PutMapping("step-update")
    public void positionupdate(@RequestBody PositionSalaryStepResponseDTO responseDTO) {
        salaryService.updatePositionSalaryStep(responseDTO);
    }

    @DeleteMapping("step-delete")
    public void positionDelete(@RequestBody PositionSalaryStepResponseDTO responseDTO) {
        salaryService.deletePositionSalaryStep(responseDTO);
    }

    @GetMapping("/step-find")
    public PositionSalaryStepResponseDTO positionSalaryStepFind(@RequestParam Long id) {
        return salaryService.findPositionSalaryStep(id);

    }

    /// AllowanceResponseDTO
    @PostMapping("/allowance-insert")
    public AllowanceResponseDTO insertAllowance(@RequestBody AllowanceResponseDTO responseDTO) {
        return salaryService.insertAllowance(responseDTO);
    }

    @GetMapping("/allowance-find-companycode")
    public List<AllowanceResponseDTO> allowanceFindCompanycode(@RequestParam("companyCode") String companyCode) {
        // 이제 반환값이 리스트!
        //return salaryService.findByAllowance_CompanyCode(companyCode);
        return  null;
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
    public AllowanceResponseDTO findAllowance(@RequestParam Long allowanceId) {
        return salaryService.findAllowance(allowanceId);
    }


    /// Deduction 서비스
    @PostMapping("deduction-insert")
    public DeductionResponseDTO insertDeduction(@RequestBody DeductionResponseDTO responseDTO) {
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
    public DeductionResponseDTO findDeduction(@RequestParam Long deductionId) {
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
