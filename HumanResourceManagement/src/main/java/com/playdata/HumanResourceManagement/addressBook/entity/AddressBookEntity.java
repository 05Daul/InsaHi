package com.playdata.HumanResourceManagement.addressBook.entity;

import com.playdata.HumanResourceManagement.company.entity.Company;
import com.playdata.HumanResourceManagement.publicEntity.DateEntity;
import com.playdata.HumanResourceManagement.publicEntity.FileEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address_books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressBookEntity extends DateEntity {

  //1
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_book_id")
  private Long addressBookId;

  // 기업명 (필수)
  @Column(name = "company_name", nullable = false)
  private String companyName;

  // 업종
  @Column(name = "industry", length = 100)
  private String industry;

  // 주소
  @Column(name = "address", length = 500)
  private String address;

  // 웹사이트 URL
  @Column(name = "website", length = 255)
  private String website;

  // 연락처 유형 (필수)
  @Column(name = "contact_type", nullable = false)
  private String contactType;

  // 전화번호 (필수)
  @Column(name = "phone_number", nullable = false, length = 50)
  private String phoneNumber;

  // 담당자 이름
  @Column(name = "contact_person_name", length = 100)
  private String contactPersonName;

  // 담당자 직책
  @Column(name = "contact_person_position", length = 100)
  private String contactPersonPosition;

  // 담당자 이메일
  @Column(name = "contact_person_email", length = 255)
  private String contactPersonEmail;

  // 파일과의 관계 매핑
  @OneToMany(mappedBy = "addressBook")
  private List<FileEntity> fileEntities;

  // 메모 (TEXT 타입)
  @Column(name = "memo", columnDefinition = "TEXT")
  private String memo;

  // 열람 권한
  @Enumerated(EnumType.STRING)
  @Column(name = "view_permission", nullable = false)
  private ViewPermission viewPermission;

  // 회사 코드 (Company 참조)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_code", referencedColumnName = "company_code")
  private Company company;

  // 주소록 업로드한 사용자 ID
  @Column(name = "uploaded_by_user_id")
  private String uploadedByUserId;

  public enum ViewPermission {
    PUBLIC,      // 전체 공개
    DEPARTMENT,  // 부서 공유
    PERSONAL     // 개인 공유
  }
}
