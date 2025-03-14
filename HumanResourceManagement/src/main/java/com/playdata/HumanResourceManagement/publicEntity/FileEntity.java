package com.playdata.HumanResourceManagement.publicEntity;

import com.playdata.HumanResourceManagement.employee.entity.Employee;
import com.playdata.HumanResourceManagement.addressBook.entity.AddressBookEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hr_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;  // 파일 고유 ID

    private String originalFileName;  // 원본 파일명
    private String storeFilename;  // 저장된 파일명
    private String filePath;  // 파일 저장 경로

    @Column(nullable = false, length = 50)
    private String category;  // 파일 카테고리 (addressBook, company, employee)

    @OneToOne(fetch = FetchType.LAZY)  // Employee와의 다대일 관계
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;  // 이 파일을 소유한 직원

    @ManyToOne(fetch = FetchType.LAZY)  // AddressBookEntity와의 다대일 관계
    @JoinColumn(name = "address_book_id", referencedColumnName = "address_book_id")
    private AddressBookEntity addressBook;  // 이 파일이 관련된 주소록

}
