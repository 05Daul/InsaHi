package com.playdata.HumanResourceManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 직원 고유 ID (PK)

    private String employeeId;  // 직원 ID (고유값)

    private String password;  // 비밀번호

    private String name;  // 직원 이름

    @Enumerated(EnumType.STRING)
    private Role role;  // 직원 역할

    private String email;  // 이메일

    private String phoneNumber;  // 전화번호

    private String address;  // 주소

    private String gender;  // 성별

    private String birthday;  // 생일

    private String departmentId;  // 부서 ID

    private String teamId;  // 팀 ID

    private String state;  // 상태 (예: 활동 중, 퇴직 등)

    @ManyToOne
    @JoinColumn(name = "company_code", referencedColumnName = "companyCode", nullable = false)
    private Company company;  // 회사와의 연관 관계

    @CreatedDate
    private Date createdAt;  // 생성일시

    @LastModifiedDate
    private Date updatedAt;  // 마지막 수정일시

    @PrePersist
    public void generateEmployeeId() {
        if (this.employeeId == null) {
            this.employeeId = UUID.randomUUID().toString().substring(0, 9);  // 직원 ID 생성
        }
    }

    // 직원 역할
    public enum Role {
        HR_MANAGER("인사 관리자"),
        CEO("대표"),
        USER("사용자");

        private final String description;

        Role(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
