package com.playdata.HumanResourceManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @Column(name = "department_id", length = 20)
    private String departmentId;  // 부서 ID (PK)

    @Column(name = "department_name", length = 100)
    private String departmentName;  // 부서명

    @Column(name = "parent_department_id")
    private Integer parentDepartmentId;  // 부모 부서 ID

    @Column(name = "department_level", columnDefinition = "INT default 0")
    private int departmentLevel = 0;  // 부서 계층

    @Column(name = "company_code", length = 100)
    private String companyCode;  // 회사 코드

    @Column(name = "left")
    private Integer left;  // 왼쪽 값

    @Column(name = "right")
    private Integer right;  // 오른쪽 값
}
