package com.playdata.ElectronicApproval.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "approvalline")
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalLineEntity {

  @Id
  private String id;
  @ManyToOne
  @JoinColumn(name = "approvalfile_id")
  private ApprovalFileEntity approvalFileEntity;
  @OneToOne
  @JoinColumn(name = "employee_id")
  private Employee employee;
  private int approvalOrder;
  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToOne(mappedBy = "approvalLine")
  private ApprovalLineDetailEntity approvalLineDetailEntity;

}
