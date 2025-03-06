package com.playdata.User.company.controller;

import com.playdata.User.company.dto.CompanyRequestDTO;
import com.playdata.User.company.dto.SignupRequestDTO;
import com.playdata.User.company.entity.Company;
import com.playdata.User.company.service.CompanyService;
import com.playdata.User.employee.dto.EmployeeRequestDTO;
import com.playdata.User.employee.entity.Employee;
import com.playdata.User.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final EmployeeService employeeService;
//    회사 정보 insert
//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@RequestBody CompanyRequestDTO companyRequestDTO) {
//        companyService.insert(companyRequestDTO);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO signupRequestDTO) {
        System.out.println("((((((((((((((((((((");
        Company savedCompany = companyService.insert(signupRequestDTO.getCompany());

        EmployeeRequestDTO employeeDTO = signupRequestDTO.getEmployee();
        employeeDTO.setCompanyCode(savedCompany.getCompanyCode());


        Employee employee = employeeService.insertEmployee(employeeDTO);

        employeeService.addAdminAndUserRoles(employee);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
