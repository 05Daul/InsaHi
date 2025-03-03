package com.playdata.User.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee") 
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    // 고유 직원 ID - 시스템
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id") 
    private Long id;  

    // 사번 - 필수
    @Column(name = "employee_number", length = 50, nullable = false, unique = true) 
    private String employeeNumber; 

    // 이름 - 필수
    @Column(name = "name", length = 100, nullable = false) 
    private String name;  

     // 근무 상태
    @Column(name = "work_status", length = 20)
    private String workStatus; 

    // 입사일
    @Column(name = "hire_date") 
    private LocalDate hireDate;  

     // 퇴사일
    @Column(name = "resignation_date")
    private LocalDate resignationDate;  

     // 이메일
    @Column(name = "email", length = 255, unique = true)
    private String email;

    // 성별
    @Column(name = "gender", length = 10)
    private String gender;  

    // 생년월일
    @Column(name = "birth_date") 
    private LocalDate birthDate; 

    // 핸드폰 번호
    @Column(name = "phone_number", length = 15, unique = true) 
    private String phoneNumber;  

    // 주소
    @Column(name = "address", columnDefinition = "TEXT") 
    private String address; 



    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "company_code") // 회사 코드 (외래키)
    // private Company company;  

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "role_id") // 역할 ID (외래키)
    // private Role role; 

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "position_id") // 직급호봉봉 ID (외래키) 
    // private Position position;
    
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "department_id") // 부서 ID (외래키)
    // private Department department;  

    // @Column(name = "profile_image_id") // 직원 이미지 파일 ID
    // private Long profileImageId;  
}
