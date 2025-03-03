package com.playdata.HumanResourceManagement.controller;

import com.playdata.HumanResourceManagement.entity.Employee;
import com.playdata.HumanResourceManagement.service.HumanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/humanResource")
public class HumanController {

    private final HumanService humanService;

    @Autowired
    public HumanController(HumanService humanService) {
        this.humanService = humanService;
    }

    // 회사 코드로 직원 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> humanList(@RequestParam String companyCode) {
        return ResponseEntity.ok(humanService.getEmployeeListByCompanyCode(companyCode));
    }

    // 권한 설정 페이지로 이동
    @GetMapping("/auth")
    public String auth() {
        return "/join/register";  // 권한 설정 페이지 반환
    }

    // 직원 검색 페이지로 이동
    @GetMapping("/search")
    public String search() {
        return "/join/search";  // 직원 검색 페이지 반환
    }

    // 직원 권한 변경
    @PutMapping("/auth/{id}")
    public ResponseEntity<Map<String, String>> changeUserRole(@PathVariable int id, @RequestBody Map<String, String> roleDetails) {
        String newRoleString = roleDetails.get("role");
        try {
            Employee.Role newRole = Employee.Role.valueOf(newRoleString.toUpperCase());
            humanService.changeUserRole(id, newRole);  // 권한 변경 처리
            return ResponseEntity.ok(Map.of("message", "직원 " + id + "의 권한이 변경되었습니다.", "newRole", newRole.name()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "잘못된 역할입니다."));
        }
    }


    // 직원 추가
    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> joinUser(@RequestBody Map<String, String> userDetails) {
        humanService.addEmployee(userDetails);
        return ResponseEntity.ok(Map.of("message", "직원이 추가되었습니다."));
    }

    // 직원 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable int id) {
        humanService.deleteEmployee(id);
        return ResponseEntity.ok(Map.of("message", "직원 " + id + "이 삭제되었습니다."));
    }

    // 직원 상세 페이지로 이동
    @GetMapping("/join/{id}")
    public String joinPage(@PathVariable int id) {
        return "/join/register";
    }

    // 직원 상세 수정 페이지로 이동
    @PutMapping("/join/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable int id, @RequestBody Map<String, String> updatedDetails) {
        humanService.updateEmployee(id, updatedDetails);  // 직원 수정 처리
        return ResponseEntity.ok(Map.of("message", "직원 " + id + "의 정보가 수정되었습니다."));
    }
}
