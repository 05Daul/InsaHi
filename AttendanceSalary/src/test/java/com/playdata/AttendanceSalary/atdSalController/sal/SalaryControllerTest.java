package com.playdata.AttendanceSalary.atdSalController.sal;

import com.playdata.AttendanceSalary.atdSalDto.sal.*;
import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionEntity;
import com.playdata.AttendanceSalary.atdSalEntity.sal.SalaryEntity;
import com.playdata.AttendanceSalary.atdSalService.sal.SalaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class SalaryControllerTest {
    @Autowired
    private SalaryService salaryService;

    @Test
    public void insertPosition() {
        Long positionId = 100L;
        String positionName = "부서1";
        String companyCode = "C001";
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setCompanyCode(companyCode);
        positionEntity.setPositionName(positionName);
        positionEntity.setPositionId(positionId);
        System.out.println("positionEntity = " + positionEntity);
    }

    @Test
    void updatePosition() {


    }

    @Test
        // 직급 삭제
    void deletePosition() {
        Long positionId = 2L;
        String positionName = "con";
        String companyCode = "C001";


    }

    @Test
    void findPosition() {
        Long positionId = 1L;
        salaryService.findPosition(positionId);

    }


    @Test
    /// PositionSalaryStep 서비스
    void insertPositionSalaryStep() {
        Long id = 2L;
        Long positionId = 3L;
        int salaryStepId= 1;
        BigDecimal baseSalary = BigDecimal.valueOf(50);
        BigDecimal positionAllowance=BigDecimal.valueOf(50);
        BigDecimal overtimeRate=BigDecimal.valueOf(50);
        int baseAnnualLeave=60;
        BigDecimal salaryIncreaseAllowance=BigDecimal.valueOf(50);
        String companyCode="C001";


    }

    @Test
    void updatePositionSalaryStep(PositionSalaryStepResponseDTO responseDTO) {

    }

    @Test
    void deletePositionSalaryStep(PositionSalaryStepResponseDTO responseDTO) {

    }

    @Test
    void findPositionSalaryStep(Long positionId) {
    }

    /// Allowance 서비스
    @Test
    void insertAllowance(AllowanceResponseDTO responseDTO) {
    }

    @Test
    void updateAllowance(AllowanceResponseDTO responseDTO) {

    }

    @Test
    void deleteAllowance(AllowanceResponseDTO responseDTO) {

    }

    @Test
    void findAllowance(Long allowanceId) {
    }

    /// Deduction 서비스
    @Test
    void insertDeduction(DeductionResponseDTO responseDTO) {
    }

    @Test
    void updateDeduction(DeductionResponseDTO responseDTO) {

    }

    @Test
    void deleteDeduction(DeductionResponseDTO responseDTO) {

    }

    @Test
    void findDeduction(Long deductionId) {
    }

    /// paystub 서비스
    @Test
    void insertPayStub(PayStubResponseDTO responseDTO) {
    }

    @Test
    void updateDeduction(PayStubResponseDTO responseDTO) {

    }

    @Test
    void deleteDeduction(PayStubResponseDTO responseDTO) {

    }

    @Test
    void findPayStub(Long payStubId) {
    }

    /// Salary 서비스
    @Test
    void insertSalary(SalaryResponseDTO responseDTO) {
    }

    @Test
    void updateSalary(SalaryResponseDTO responseDTO) {

    }

    @Test
    void deleteSalary(SalaryResponseDTO responseDTO) {

    }

    @Test
    void findSalary(Long salaryId) {
    }


}
