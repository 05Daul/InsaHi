package com.playdata.HumanResourceManagement.company.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
import com.playdata.HumanResourceManagement.employee.entity.Employee;

@Entity
@Table(name = "company")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @Column(name = "company_code", unique = true, length = 36)
    private String companyCode;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "head_count")
    private String headCount;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "business_number")
    private String businessNumber;

    @Column(name = "start_time")
    private Timestamp startTime;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DepartmentEntity> departments; // 부서 목록

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Employee> employees; // 직원 목록

    @PrePersist
    public void generateCompanyCode() {
        if (this.companyCode == null) {
            this.companyCode = "Com"+UUID.randomUUID().toString().substring(0, 4);
        }
    }
}
