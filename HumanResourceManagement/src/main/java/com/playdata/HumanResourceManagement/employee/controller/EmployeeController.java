package com.playdata.HumanResourceManagement.employee.controller;

import com.playdata.HumanResourceManagement.employee.authentication.TokenManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.time.LocalTime;
import org.springframework.web.bind.annotation.*;
import com.playdata.HumanResourceManagement.employee.dto.LoginDTO;
import com.playdata.HumanResourceManagement.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeResponseDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final TokenManager tokenManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        System.out.println("loginDTO: " + loginDTO);
        //1. 사용자정보를 담은 인증객체생성
        //2. 인증처리
        Authentication authentication =  employeeService.login(loginDTO);
        //3. 인증이 완료되면 인증객체를 이용해서 토큰생성하기
        String jwt = tokenManager.createToken(authentication);

        System.out.println("jwt: " + jwt);

        //4. 응답헤더에 토큰을 내보내기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + jwt);

        System.out.println("성공 !!!!!!!~~~~~~~~~~~~~~~~~!!!!!!!!!!!!!!");
        return new ResponseEntity<>(jwt,headers, HttpStatus.OK);
    }


    @GetMapping("/{employeeId}/company/start-time")
    public ResponseEntity<LocalTime> getCompanyStartTime(
            @PathVariable("employeeId") String employeeId) {
        LocalTime startTime = employeeService.findCompanyStartTimeByEmployeeId(employeeId);
        log.info("controller 단 : getCompanyStartTime: {}", startTime);
        return ResponseEntity.ok(startTime);
    }


    @GetMapping("/find")
    /// 김다울
    public EmployeeResponseDTO findEmployee(@RequestParam("employeeId") String employeeId) {
        return employeeService.findEmployeeById(employeeId);
    }



    @GetMapping("/getallemployeeids")
    public List<String> getAllEmployeeIds() {
        return employeeService.getAllEmployeeIds();
    }


}