package com.playdata.ElectronicApproval.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "approvalfile")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApprovalFileEntity {

  @Id
  private String id;
  private String name;
  private String text;
  @ManyToOne
  @JoinColumn(name = "approvalform_id")// ApprovalForm의 PK를 참조하는 외래 키
  private ApprovalFormEntity approvalForm;
  @Enumerated(EnumType.STRING)
  private ApprovalStatus status;

  @Enumerated(EnumType.STRING)
  private DeleteStatus deleteStatus;//delete_status

  private boolean deleted;

  private String companyCode;

//  @ManyToOne
//  @JoinColumn(name = "employee_id")
//  private Employee employee;

  private String referenceIds; //참조인 "id1, id2, ..."

  @OneToMany(mappedBy = "approvalFile", cascade = CascadeType.ALL)
  private List<ApprovalLineEntity> approvalLineEntities;
}


