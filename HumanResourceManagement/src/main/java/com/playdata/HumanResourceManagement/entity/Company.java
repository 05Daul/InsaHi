package com.playdata.HumanResourceManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "companys")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 회사 고유 ID (PK)

    @Column(unique = true)
    private String companyCode;  // 회사 코드 (고유값)

    private String companyName;  // 회사 이름

    private String companyImage;  // 회사 이미지 URL

    private String companyAddress;  // 회사 주소

    private String headCount;  // 회사의 직원 수

    private String category;  // 회사의 카테고리

    private Date createdAt;  // 생성일시

    private String businessNumber;  // 사업자 등록 번호

    private Timestamp startTime;  // 시작 시간

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees;  // 회사와 관련된 직원 목록
}
