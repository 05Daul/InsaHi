package com.playdata.ElectronicApproval.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "approvalform")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApprovalFormEntity {

  @Id
  private String id;
  private String name;
  private String companyCode;

}
