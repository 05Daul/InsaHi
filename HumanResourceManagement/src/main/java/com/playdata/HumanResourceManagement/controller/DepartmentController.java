package com.playdata.HumanResourceManagement.controller;

import com.playdata.HumanResourceManagement.entity.Department;
import com.playdata.HumanResourceManagement.service.department.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // 1. 부서 수정
    @PutMapping("/update/{departmentId}")
    public ResponseEntity<Map<String, String>> modifyDepartment(@PathVariable String departmentId, @RequestBody Department updatedDepartment) {
        Optional<Department> updated = Optional.ofNullable(departmentService.updateDepartment(departmentId, updatedDepartment));
        return updated.map(department ->
                        ResponseEntity.ok(Map.of("message", "부서 정보가 수정되었습니다.", "departmentId", department.getDepartmentId())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "부서가 존재하지 않습니다.")));
    }

    // 2. 부서 조회 (부서 ID)
    @GetMapping("/{departmentId}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable String departmentId) {
        return departmentService.getDepartmentById(departmentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // 3. 부서 삭제 (부서 ID)
    @DeleteMapping("/delete/{departmentId}")
    public ResponseEntity<Map<String, String>> deleteDepartment(@PathVariable String departmentId) {
        try {
            departmentService.deleteDepartment(departmentId);
            return ResponseEntity.ok(Map.of("message", "부서가 삭제되었습니다.", "departmentId", departmentId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // 4. 회사 코드로 모든 부서 조회
    @GetMapping("/all/{companyCode}")
    public ResponseEntity<List<Department>> getAllDepartments(@PathVariable String companyCode) {
        List<Department> departments = departmentService.getDepartmentsByCompanyCode(companyCode);
        if (departments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 부서가 없으면 404 반환
        }
        return ResponseEntity.ok(departments);
    }

    // 5. 회사 코드와 부서 계층으로 부서 조회
    @GetMapping("/level/{companyCode}/{departmentLevel}")
    public ResponseEntity<List<Department>> getDepartmentsByLevel(@PathVariable String companyCode, @PathVariable int departmentLevel) {
        List<Department> departments = departmentService.getDepartmentsByLevel(companyCode, departmentLevel);
        return departments.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                : ResponseEntity.ok(departments);
    }
}
