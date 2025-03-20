package com.playdata.HumanResourceManagement.employee.controller;

import com.playdata.HumanResourceManagement.employee.authentication.TokenManager;
import com.playdata.HumanResourceManagement.employee.dto.ProfileCardDTO;
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
        EmployeeResponseDTO employeeResponseDTO = employeeService.findEmployeeById(employeeId);
        return employeeResponseDTO;
    }



    @GetMapping("/getallemployeeids")
    public List<String> getAllEmployeeIds() {
        return employeeService.getAllEmployeeIds();
    }


    //mypage 왼쪽 작은 프로필
    @GetMapping("/{employeeId}/profilecard")
    public ResponseEntity<ProfileCardDTO> getProfileCard(@PathVariable("employeeId") String employeeId) {
        ProfileCardDTO response = employeeService.getProfileCard(employeeId);
        System.out.println("result result result : "+response);
        return ResponseEntity.ok(response);

    }
    //개인정보 수정페이지
    @GetMapping("/{employeeId}/employeeinfo")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeInfo(@PathVariable("employeeId") String employeeId) {
        EmployeeResponseDTO response = employeeService.getEmployeeInfo(employeeId);

        return ResponseEntity.ok(response);
    }


}