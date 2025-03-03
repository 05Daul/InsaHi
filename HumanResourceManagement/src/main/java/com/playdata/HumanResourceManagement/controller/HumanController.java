package com.playdata.HumanResourceManagement.controller;

import com.playdata.HumanResourceManagement.entity.Employee;
import com.playdata.HumanResourceManagement.service.human.HumanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        try {
            return ResponseEntity.ok(humanService.getEmployeeListByCompanyCode(companyCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "직원 목록 조회 실패", "error", e.getMessage()));
        }
    }

    // 권한 설정 페이지로 이동
    @GetMapping("/auth")
    public ResponseEntity<String> auth() {
        return ResponseEntity.ok("/join/register");
    }

    // 직원 검색 페이지로 이동
    @GetMapping("/search")
    public ResponseEntity<String> search() {
        return ResponseEntity.ok("/join/search");
    }

    // 직원 권한 변경
    @PutMapping("/auth/{id}")
    public ResponseEntity<Map<String, String>> changeUserRole(@PathVariable String id, @RequestBody Map<String, String> roleDetails) {  // 🔄 int → String
        String newRoleString = roleDetails.get("role");
        try {
            Employee.Role newRole = Employee.Role.valueOf(newRoleString.toUpperCase());
            humanService.changeUserRole(id, newRole);  // 🔄 int → String
            return ResponseEntity.ok(Map.of("message", "직원 " + id + "의 권한이 변경되었습니다.", "newRole", newRole.name()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "잘못된 역할입니다.", "error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    // 직원 추가
    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> joinUser(@RequestBody Map<String, String> userDetails) {
        try {
            humanService.addEmployee(userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "직원이 추가되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "직원 추가 실패", "error", e.getMessage()));
        }
    }

    // 직원 삭제
    @DeleteMapping("/delete/{companyCode}/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(
            @PathVariable String companyCode,
            @PathVariable String id) {
        try {
            humanService.deleteEmployee(companyCode, id);
            return ResponseEntity.ok(Map.of("message", "직원 " + id + "이 삭제되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "직원 삭제 실패", "error", e.getMessage()));
        }
    }


    // 직원 상세 페이지로 이동
    @GetMapping("/join/{id}")
    public ResponseEntity<String> joinPage(@PathVariable String id) {  // 🔄 int → String
        return ResponseEntity.ok("/join/register");
    }

    // 직원 상세 수정
    @PutMapping("/join/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable String id, @RequestBody Map<String, String> updatedDetails) {  // 🔄 int → String
        try {
            humanService.updateEmployee(id, updatedDetails);  // 🔄 int → String
            return ResponseEntity.ok(Map.of("message", "직원 " + id + "의 정보가 수정되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "직원 수정 실패", "error", e.getMessage()));
        }
    }
}
