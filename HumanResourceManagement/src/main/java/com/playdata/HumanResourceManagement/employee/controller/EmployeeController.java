package com.playdata.HumanResourceManagement.employee.controller;

import com.playdata.HumanResourceManagement.company.entity.Company;
import com.playdata.HumanResourceManagement.company.repository.CompanyRepository;
import com.playdata.HumanResourceManagement.employee.authentication.TokenManager;
import com.playdata.HumanResourceManagement.employee.dto.AuthorityResponseDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmpAuthResponseDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeRequestDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeResponseAtdSalDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeResponseDTO;
import com.playdata.HumanResourceManagement.employee.dto.EmployeeUpdateDTO;
import com.playdata.HumanResourceManagement.employee.dto.LoginDTO;
import com.playdata.HumanResourceManagement.employee.dto.MyUserDetail;
import com.playdata.HumanResourceManagement.employee.dto.ProfileCardDTO;
import com.playdata.HumanResourceManagement.employee.dto.UpdatePasswordDTO;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import com.playdata.HumanResourceManagement.employee.service.EmployeeEmailService;
import com.playdata.HumanResourceManagement.employee.service.EmployeeService;
import jakarta.mail.MessagingException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

  private final EmployeeService employeeService;
  private final TokenManager tokenManager;
  private final EmployeeEmailService employeeEmailService;
  private final CompanyRepository companyRepository;

  @PutMapping("/update-salary-step")
  public void updateSalaryStep(@RequestParam("employeeId") String employeeId,
      @RequestParam("positionSalaryId") Long positionSalaryId) {
    employeeService.updateEmployee(employeeId, positionSalaryId);
  }

  @GetMapping("/auth-list")
  public List<EmpAuthResponseDTO> getEmployeesByCompanyAndAuthority(
      @RequestParam("companyCode") String companyCode,
      @RequestParam("authorityName") String authorityName) {

    List<AuthorityResponseDTO> authorityList = employeeService.findAuthoritiesByCompanyCode(
        companyCode);

    boolean exists = authorityList.stream()
        .anyMatch(auth -> auth.getAuthorityName().equals(authorityName));
    if (!exists) {
      return List.of(); // 빈 리스트
    }

    List<EmpAuthResponseDTO> allByAuthority = employeeService.findByAuthorityList_AuthorityName(
        authorityName);

    List<EmpAuthResponseDTO> result = allByAuthority.stream()
        .filter(emp -> emp.getCompanyCode().equals(companyCode))
        .collect(Collectors.toList());

    return result;
  }

  //login
  @PostMapping("/login")
  public ResponseEntity<Map> login(@RequestBody LoginDTO loginDTO) {
    System.out.println("loginDTO: " + loginDTO);
    //1. 사용자정보를 담은 인증객체생성
    //2. 인증처리
    Authentication authentication = employeeService.login(loginDTO);
    //3. 인증이 완료되면 인증객체를 이용해서 토큰생성하기
    String jwt = tokenManager.createToken(authentication);
    MyUserDetail myUserDetail = (MyUserDetail) authentication.getPrincipal();

    System.out.println("jwt: " + jwt + ",길이" + myUserDetail.getUsername().length());

    //4. 응답헤더에 토큰을 내보내기
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + jwt);

    System.out.println("성공 !!!!!!!~~~~~~~~~~~~~~~~~!!!!!!!!!!!!!!");
    return new ResponseEntity<>(Map.of(
        "jwt", jwt,
        "employeeId", myUserDetail.getUsername(),
        "companyCode", myUserDetail.getCompanyCode(),
        "auth", myUserDetail.getAuthorities().stream()
            //각 GrantedAuthority 객체에서 문자열 권한 값을 추출
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","))), headers, HttpStatus.OK);

  }


  @GetMapping("/{employeeId}/company/start-time")
  public LocalTime getCompanyStartTime(
      @PathVariable("employeeId") String employeeId) {
    LocalTime startTime = employeeService.findCompanyStartTimeByEmployeeId(employeeId);
    log.info("controller 단 : getCompanyStartTime: {}", startTime);

    return startTime;
  }

  @GetMapping(value = "/find", produces = "application/json")
  public ResponseEntity<EmployeeResponseAtdSalDTO> findEmployee(
      @RequestParam("employeeId") String employeeId) {
    EmployeeResponseDTO employeeResponseDTO = employeeService.findEmployeeById(employeeId);
    if (employeeResponseDTO == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 직원을 찾을 수 없습니다.");
    }
    EmployeeResponseAtdSalDTO responseAtdSalDTO = new EmployeeResponseAtdSalDTO(
        employeeResponseDTO.getName(),
        employeeResponseDTO.getEmployeeId(), employeeResponseDTO.getRole(),
        employeeResponseDTO.getCompanyCode(), employeeResponseDTO.getPositionSalaryId(),
        employeeResponseDTO.getHireDate());
    return ResponseEntity.ok(responseAtdSalDTO);
  }

//  @GetMapping(value = "/find")
//  /// 김다울
//  public EmployeeResponseDTO findEmployee(@RequestParam("employeeId") String employeeId) {
//    EmployeeResponseDTO employeeResponseDTO = employeeService.findEmployeeById(employeeId);
//    return employeeResponseDTO;
//  }


  @GetMapping("/getallemployeeids")
  public List<String> getAllEmployeeIds() {
    return employeeService.getAllEmployeeIds();
  }


  //mypage 왼쪽 작은 프로필
  @GetMapping("/{employeeId}/profilecard")
  public ResponseEntity<ProfileCardDTO> getProfileCard(
      @PathVariable("employeeId") String employeeId) {
    ProfileCardDTO response = employeeService.getProfileCard(employeeId);
    System.out.println("result result result : " + response);

    return ResponseEntity.ok(response);

  }

  //개인정보 수정페이지 출력
  @GetMapping("/{employeeId}/employeeinfo")
  public ResponseEntity<EmployeeResponseDTO> getEmployeeInfo(
      @PathVariable("employeeId") String employeeId) {
    EmployeeResponseDTO response = employeeService.getEmployeeInfo(employeeId);

    return ResponseEntity.ok(response);
  }

  //개인정보 수정페이지 업데이트
  @PutMapping(value = "/{employeeId}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeResponseDTO> updateEmployeeInfo(
      @PathVariable("employeeId") String employeeId,
      @RequestPart("email") String email,
      @RequestPart("phoneNumber") String phoneNumber,
      @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {

    System.out.println("🔥🔥🔥 요청 들어옴 @PutMapping /employee/{employeeId}/update");
    System.out.println("email: " + email);
    System.out.println("phone: " + phoneNumber);
    System.out.println("image null?: " + (profileImage == null));

    EmployeeUpdateDTO updateDTO = new EmployeeUpdateDTO(email, phoneNumber);
    EmployeeResponseDTO response = employeeService.updateEmployeeInfo(employeeId, updateDTO,
        profileImage);
    System.out.println("fffffffuuuuuucccccckkkkkk update update hihihihihi" + response);

    System.out.println("개인정보 수정페이지 수정 수정 수정 수정" + response);
    System.out.println("개인정보 수정페이지 수정 수정 수정 수정" + response);
    System.out.println("개인정보 수정페이지 수정 수정 수정 수정" + response);
    System.out.println("개인정보 수정페이지 수정 수정 수정 수정" + response);

    return ResponseEntity.ok(response);
  }

  @PutMapping("/{employeeId}/updatepassword")
  public ResponseEntity<EmployeeResponseDTO> updateEmployeePassword(
      @PathVariable("employeeId") String employeeId,
      @RequestBody UpdatePasswordDTO updatePasswordDTO) {
    EmployeeResponseDTO response =
        employeeService.updatePassword(
            employeeId, updatePasswordDTO);

    return ResponseEntity.ok(response);
  }

  //회원등록
  @PostMapping("/insertemployee")
  public ResponseEntity<?> insertEmployee(
      @RequestBody EmployeeRequestDTO employeeRequestDTO) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Object principal = authentication.getPrincipal();

    if (!(principal instanceof MyUserDetail myUserDetail)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
    }

    List<String> authorities = myUserDetail.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    if (!(authorities.contains("ROLE_ADMIN") && authorities.contains("ROLE_USER"))) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "등록 권한이 없습니다."));
    }

    employeeRequestDTO.setCompanyCode(myUserDetail.getCompanyCode());

    Employee employee = employeeService.employeeInsert(employeeRequestDTO);
    employeeService.addUserRoles(employee);
    Optional<Company> optionalCompany = companyRepository.findByCompanyCode(
        employee.getCompanyCode());
    Company company = optionalCompany.get();

    // 회원가입 완료 후 이메일 전송
    try {
      employeeEmailService.sendRegistrationInfo(employee.getName(), employee.getEmail(),
          employee.getCompanyCode(), employee.getEmployeeId(),
          company.getCompanyName());

    } catch (MessagingException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("직원등록은 완료되었으나 이메일 전송 중 오류가 발생했습니다.");
    }

    System.out.println(
        "employeeDTO controller controller controller !~~~@!~@~" + employeeRequestDTO);

    return ResponseEntity.ok(Map.of("message", "직원등록 성공! spring ~ controller ~"));
  }

  @PostMapping("/grant-admin")
  public ResponseEntity<String> grantAdmin(@RequestParam("employeeId") String employeeId) {
    employeeService.addAdminRoleToEmployee(employeeId);
    return ResponseEntity.ok("관리자 권한이 부여됐습니다.");
  }

  @DeleteMapping("/delete-admin")
  public ResponseEntity<String> revokeAdmin(@RequestParam("employeeId") String employeeId) {
    employeeService.removeAdminRoleFromEmployee(employeeId);
    return ResponseEntity.ok("관리자 권한이 삭제됐습니다.");
  }

  //    채팅방 초대 멤버 조회
  @GetMapping("/all")
  public List<Employee> getMemberList() {
    return employeeService.getMemberList();
  }

  @GetMapping("/allEmp")
  public List<EmployeeResponseAtdSalDTO> getEmployeeList() {
    
    return employeeService.getMemberList()
        .stream()
        .map(employee -> {
          log.info(employee.toString());
          return new EmployeeResponseAtdSalDTO(employee.getName(), employee.getEmployeeId(), null,
              employee.getCompanyCode(), null, null);
        })
        .collect(Collectors.toList());
  }
}