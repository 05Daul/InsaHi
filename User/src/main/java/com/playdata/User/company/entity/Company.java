package com.playdata.User.company.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @Column(unique = true, length = 36)

    private String companyCode; //1

    private String companyName; //2
//    private String companyImage;
    private String companyAddress; //3
    private String headCount; //4
    private Date createdAt; // 설립일 입력 받아서 저장
    private String businessNumber;
    private LocalTime startTime;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees;

    @PrePersist
    public void generateCompanyCode() {
        if (this.companyCode == null) {
            this.companyCode = UUID.randomUUID().toString().substring(0, 9);
        }
    }

//    public Company(String companyName, String companyCode, String headCount, String businessNumber) {
//        this.companyName = companyName;
//        this.companyCode = companyCode;
//        this.headCount = headCount;
//        this.businessNumber = businessNumber;
//    }


}
