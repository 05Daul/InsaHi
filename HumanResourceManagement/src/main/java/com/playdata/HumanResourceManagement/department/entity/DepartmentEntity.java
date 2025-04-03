package com.playdata.HumanResourceManagement.department.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.playdata.HumanResourceManagement.company.entity.Company;
import com.playdata.HumanResourceManagement.employee.entity.Employee;
import com.playdata.HumanResourceManagement.publicEntity.DateEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "departments")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "departmentId")
@ToString(onlyExplicitlyIncluded = true)
public class DepartmentEntity extends DateEntity {

  @Id
  @Column(name = "department_id", length = 20)
  private String departmentId;

  @Column(name = "department_name", length = 100, nullable = false)
  private String departmentName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_department_id", referencedColumnName = "department_id")
  @JsonBackReference
  private DepartmentEntity parentDepartmentId;

  @Column(name = "company_code", length = 100, nullable = false)
  private String companyCode;

  @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = false)
  @JsonManagedReference
  private List<Employee> employees;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_code", referencedColumnName = "company_code", insertable = false, updatable = false)
  @JsonBackReference
  private Company company;

  // 하위 부서들
  @OneToMany(mappedBy = "parentDepartmentId", cascade = CascadeType.ALL, orphanRemoval = false)
  @JsonManagedReference
  private List<DepartmentEntity> subDepartments = new ArrayList<>(); // 기본값 빈 리스트로 초기화

  @PrePersist
  public void setDefaultDepartmentId() {
    if (this.departmentId == null || this.departmentId.isEmpty()) {
      this.departmentId = "Dept" + UUID.randomUUID().toString().substring(0, 8); // 8자리 UUID
    }
  }


  // Getter에서 null 처리하여 빈 리스트로 초기화
  public List<DepartmentEntity> getSubDepartments() {
    if (subDepartments == null) {
      subDepartments = new ArrayList<>();
    }
    return subDepartments;
  }
}