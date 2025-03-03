package com.playdata.HumanResourceManagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)  // 엔티티 리스너 추가
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeId;

    @JsonIgnore  // 비밀번호 JSON 응답에서 제외
    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;
    private String phoneNumber;
    private String address;

    @Enumerated(EnumType.STRING)  // Enum 사용
    private Gender gender;

    private String birthday;

    private String departmentId;
    private String teamId;
    private String state;

    @ManyToOne
    @JoinColumn(name = "company_code", referencedColumnName = "companyCode", nullable = false)
    private Company company;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @PrePersist
    public void generateEmployeeId() {
        if (this.employeeId == null) {
            this.employeeId = UUID.randomUUID().toString();  // 전체 UUID 사용
        }
    }

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

    public enum Gender {
        MALE, FEMALE, OTHER  // 성별을 Enum으로 관리
    }
}
