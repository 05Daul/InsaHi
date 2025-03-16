package com.playdata.AttendanceSalary.atdClient.hrmDTO;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

    private String employeeId;
    private LocalTime startTime;
    private String password; // 1(default 1234) 비밀번호
    private String name; //2 이름
    private String email; //3 이메일
    private String phoneNumber; //4 전화번호
    private String address; //5 주소
    private String departmentId; // 부서
    private String teamId;
    private String state; // 상태 (Active, Inactive 등)
    private Long positionSalaryId; //직급호봉
    private String companyCode;
    private LocalDate hireDate;
    private LocalDate retireDate;
}
