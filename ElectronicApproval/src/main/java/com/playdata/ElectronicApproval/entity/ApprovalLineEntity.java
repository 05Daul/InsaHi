package com.playdata.ElectronicApproval.entity;

import jakarta.persistence.*;
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
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @ManyToOne
  @JoinColumn(name = "approvalfile_id")
  private ApprovalFileEntity approvalFile;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "approvallinedetail_id")
  private ApprovalLineDetailEntity approvalLineDetail;

  private String employeeId;

  private int approvalOrder;

  private String companyId;
}
