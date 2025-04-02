package com.playdata.HumanResourceManagement.employee.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.playdata.HumanResourceManagement.company.entity.Company;
import com.playdata.HumanResourceManagement.department.business.entity.DepartmentEntity;
import com.playdata.HumanResourceManagement.publicEntity.FileEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee")
@ToString(onlyExplicitlyIncluded = true)
public class Employee {

  @Id
  @Column(name = "employee_id", unique = true, length = 36)
  private String employeeId;

  @Column(name = "start_time")
  private LocalTime startTime;

  @Column(nullable = true)
  private String password;

  private String name; //2 이름
  private String email; //3 이메일
  private String phoneNumber; //4 전화번호

  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "department_id", nullable = true)  // 외래키 컬럼만 지정
  @JsonBackReference  // 순환 참조 방지
  private DepartmentEntity department; // 부서
  private String state; // 상태 (Active, Inactive 등)
  private Long positionSalaryId; //직급호봉
  private Date hireDate;
  private LocalDate retireDate;


  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "company_code", referencedColumnName = "company_code", nullable = true)
  @JsonBackReference  // 순환 참조 방지
  private Company company; // 회사

  @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//  @JoinColumn(name = "profile_image", referencedColumnName = "profile_image", nullable = true)
  private FileEntity profileImage;  // 프로필 이미지

  @ManyToMany
  @JoinTable(
      name = "employee_authority",
      joinColumns = {@JoinColumn(name = "employee_id", referencedColumnName = "employee_id")},
      inverseJoinColumns = {
          @JoinColumn(name = "authority_id", referencedColumnName = "authority_id")}
  )
  private Set<Authority> authorityList;  // 권한 목록

  // 권한 이름만 반환
  public Set<String> getAuthorityNames() {
    return authorityList.stream()
        .map(Authority::getAuthorityName)
        .collect(Collectors.toSet());
  }

  // 부서 ID 반환 (부서가 없는 경우 null 반환)
  public String getDepartmentId() {
    return department != null ? department.getDepartmentId() : null;
  }

  /// 직급명 설정 (직급 ID로 외부에서 직급명 조회)
//    public void setPositionNameFromClient() {
//        if (this.positionSalaryId != null) {
//            this.positionSalaryIdName = fetchPositionNameFromClient(positionId);
//        }
//    }

  // 외부 시스템에서 직급명 조회 (예시: API 호출)
  private String fetchPositionNameFromClient(String positionId) {
    return "직급명";  // 외부 시스템에서 직급명을 조회해야 하는 부분
  }

  // 엔티티 저장 전에 기본값 설정
  @PrePersist
  public void prePersist() {
    if (this.password == null) {
      this.password = "1234";  // 기본 비밀번호 설정
    }
    if (this.authorityList == null) {
      this.authorityList = new HashSet<>();  // 권한 목록 초기화
    }
    if (this.employeeId == null) {
      this.employeeId = "2025" + UUID.randomUUID().toString()
          .substring(0, 4);  // UUID로 employeeId 생성 (전체 UUID 사용)
    }
  }

  public String getCompanyCode() {
    return company != null ? company.getCompanyCode() : null;
  }

  // 부서 이름 반환 (부서가 없는 경우 "Department is null" 반환)
  public String getDepartmentInfo() {
    return department != null ? department.getDepartmentName() : "Department is null";
  }

  // 부서 변경
  public void changeDepartment(DepartmentEntity newDepartment) {
    this.department = newDepartment;
  }

  // 상태 반환 (상태가 없는 경우 "Status not available" 반환)
  public String getStatus() {
    return state != null ? state : "Status not available";
  }

  /// 직급 반환 (직급이 없는 경우 "Position not available" 반환)
//    public String getPosition() {
//        return positionName != null ? positionName : "Position not available";
//    }
}
