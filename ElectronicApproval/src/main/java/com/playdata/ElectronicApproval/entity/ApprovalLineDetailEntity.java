package com.playdata.ElectronicApproval.entity;


import com.playdata.ElectronicApproval.common.publicEntity.DateEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "approvallinedetail")
public class ApprovalLineDetailEntity extends DateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Enumerated(EnumType.STRING)
  private ApprovalStatus status;

  //결제일시는 updatedtime
  //private Date approvalDate;
  private String reason;

  private String companyCode;

//  5955942d-6bf6-43b3-b6aa-c5d11acd7bbe_detail_3
}
