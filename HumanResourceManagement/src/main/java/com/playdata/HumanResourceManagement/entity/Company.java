package com.playdata.HumanResourceManagement.entity;

import jakarta.persistence.*;  // JPA 패키지로 통일
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;  // 변경: LocalTime → LocalDateTime
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "companys")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Company {

    @Id  // JPA 어노테이션만 사용
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String companyCode;

    private String companyName;
    private String companyImage;
    private String companyAddress;

    private Integer headCount;

    private String category;

    @Temporal(TemporalType.TIMESTAMP)  // 추가: 날짜 타입 명시
    @CreatedDate  // 자동 생성일시
    private Date createdAt;

    private String businessNumber;

    private LocalDateTime startTime;  // 변경: LocalTime → LocalDateTime

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees;
}
