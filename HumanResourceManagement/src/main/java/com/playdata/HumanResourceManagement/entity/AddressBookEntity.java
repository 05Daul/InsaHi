package com.playdata.HumanResourceManagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "address_books")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddressBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_book_id")
    private Long addressBookId;  // 고유 아이디 (PK)

    @Column(name = "company_name", nullable = false)
    private String companyName;  // 기업명 (not null)

    @Column(name = "industry", length = 100)
    private String industry;  // 업종

    @Column(name = "address", length = 500)
    private String address;  // 주소

    @Column(name = "website", length = 255)
    private String website;  // 웹사이트 URL

    @Column(name = "contact_type", nullable = false)
    private String contactType;  // 연락처 유형

    @Column(name = "phone_number", nullable = false, length = 50)
    private String phoneNumber;  // 전화번호 (not null)

    @Column(name = "contact_person_name", length = 100)
    private String contactPersonName;  // 담당자 이름

    @Column(name = "contact_person_position", length = 100)
    private String contactPersonPosition;  // 담당자 직책

    @Column(name = "contact_person_email", length = 255)
    private String contactPersonEmail;  // 담당자 이메일

    @ManyToOne
    @JoinColumn(name = "file_id", referencedColumnName = "fileId")
    private FileEntity fileEntity;  // 파일 아이디

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo;  // 메모

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;  // 생성일시

    @Enumerated(EnumType.STRING)
    @Column(name = "view_permission", nullable = false)
    private ViewPermission viewPermission;  // 열람 권한

    @Column(name = "company_code", length = 100)
    private String companyCode;  // 회사코드

    // 열람 권한을 관리하는 Enum 타입
    public enum ViewPermission {
        PUBLIC,      // 전체 공개
        DEPARTMENT,  // 부서 공유
        PERSONAL     // 개인 공유
    }
}
