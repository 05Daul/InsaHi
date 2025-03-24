package com.playdata.HumanResourceManagement.company.controller;

import com.playdata.HumanResourceManagement.company.dto.SignupRequestDTO;
import com.playdata.HumanResourceManagement.company.entity.Company;
import com.playdata.HumanResourceManagement.company.service.CompanyService;
import com.playdata.HumanResourceManagement.company.service.EmailService;
import com.playdata.HumanResourceManagement.employee.dto.AdminRequestDTO;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import com.playdata.HumanResourceManagement.employee.service.EmployeeService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final EmailService emailService;

    //회사 && 대표자 정보 입력
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequestDTO) {

        //회사 정보 주입
        Company savedCompany = companyService.insert(signupRequestDTO.companySignupRequestDTO());

        //companyCode를 주입받아 대표자 정보 입력
        AdminRequestDTO employeeDTO = signupRequestDTO.AdminSignupRequestDTO();
        employeeDTO.setCompanyCode(savedCompany.getCompanyCode());

        //권한 자동 입력
        Employee employee = employeeService.adminInsert(employeeDTO);
        employeeService.addAdminAndUserRoles(employee);

        // 회원가입 완료 후 이메일 전송
        try {
            emailService.sendRegistrationInfo(employee.getName(),employee.getEmail(), savedCompany.getCompanyCode(), employee.getEmployeeId());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입은 완료되었으나 이메일 전송 중 오류가 발생했습니다.");
        }

        System.out.println("=====================================");
        System.out.println(signupRequestDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
