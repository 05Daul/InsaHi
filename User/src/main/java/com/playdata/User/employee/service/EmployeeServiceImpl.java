package com.playdata.User.employee.service;

import com.playdata.User.company.entity.Company;
import com.playdata.User.company.repository.CompanyRepository;
import com.playdata.User.employee.dao.EmployeeDAO;
import com.playdata.User.employee.dto.EmployeeRequestDTO;
import com.playdata.User.employee.dto.LoginDTO;
import com.playdata.User.employee.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    public void insertEmployee(EmployeeRequestDTO employeeRequestDTO) {

//        Company company = companyRepository.findByCompanyCode(employeeRequestDTO.getCompanyCode())
//                .orElseThrow(() -> new RuntimeException("해당 companyCode를 가진 회사가 없습니다."));

        Employee entity = modelMapper.map(employeeRequestDTO, Employee.class);
//        entity.setCompany(company);
        employeeDAO.insert(entity);
    }

//    @Override
//    public Authentication signin(LoginDTO employee) {
//        //스프링시큐리티의 인증이 실행되도록 처리
//        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//        //1.  사용자정보를 이용해서 UsernamePasswordAuthenticationToken객체생성
//        UsernamePasswordAuthenticationToken token =
//                new UsernamePasswordAuthenticationToken
//                        (employee.getEmployeeId(), employee.getPassword());//companyCode 추가해야함
//        //2. SpringSecurity인증시스템을 동작할 수 있도록 작업
//        // authenticationManagerBuilder.getObject()가  AuthenticationManager객체반환
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);
//
//        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//        return authentication;
//
//    }
}
