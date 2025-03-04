package com.playdata.ElectronicApproval.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
@Table(name = "approvalform")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApprovalFormEntity {

  @Id
  private String id;
  private String name;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToMany(mappedBy = "approvalForm", cascade = CascadeType.ALL)
  private List<ApprovalFileEntity> approvalFileEntities;

}
