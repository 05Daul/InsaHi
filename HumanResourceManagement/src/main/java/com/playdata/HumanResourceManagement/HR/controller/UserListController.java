package com.playdata.HumanResourceManagement.HR.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{companyCode}/hr")
public class UserListController {



    // 1. 사용자 리스트 조회
    @GetMapping("/list")
    public String list(@PathVariable("companyCode") String companyCode) {
        // companyCode를 사용하여 비즈니스 로직 처리
        return "User List for company code: " + companyCode;
    }

    // 2. 사용자 상세 정보 조회
    @GetMapping("/{employeeId}")
    public String detailUser(@PathVariable("companyCode") String companyCode, @PathVariable("employeeId") String employeeId) {
        // companyCode와 employeeId를 사용하여 비즈니스 로직 처리
        return "User details for employee " + employeeId + " in company " + companyCode;
    }

    // 3. 사용자 정보 수정
    @PutMapping("/detail/{id}/edit")
    public String editUser(@PathVariable("companyCode") String companyCode, @PathVariable("id") String id) {
        // companyCode와 id를 사용하여 비즈니스 로직 처리
        return "Edit user " + id + " in company " + companyCode;
    }

    // 4. 사용자 생성
    @GetMapping("/create")
    public String createUser(@PathVariable("companyCode") String companyCode) {
        // companyCode를 사용하여 사용자 생성 로직 처리
        return "Create user in company " + companyCode;
    }

    //?DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("companyCode") String companyCode, @PathVariable("id") String id) {
        // companyCode와 id를 사용하여 비즈니스 로직 처리
        return "Delete user " + id + " in company " + companyCode;
    }
}
