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
    private LocalDate hireDate;
    private String employeeId;
    private String password;
    private String name;
    private String role;
    private String companyCode;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String birthday;
    private String departmentId;
    private String teamId;
    private String state;
    private Long positionSalaryId;
    private  LocalTime retireDate;


}

