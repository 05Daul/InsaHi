package com.playdata.HumanResourceManagement.company.controller;

import com.playdata.HumanResourceManagement.company.dto.SignupRequestDTO;
import com.playdata.HumanResourceManagement.company.entity.Company;
import com.playdata.HumanResourceManagement.company.service.CompanyService;
import com.playdata.HumanResourceManagement.employee.dto.AdminRequestDTO;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import com.playdata.HumanResourceManagement.employee.service.EmployeeService;
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
        System.out.println("=====================================");
        System.out.println(signupRequestDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
