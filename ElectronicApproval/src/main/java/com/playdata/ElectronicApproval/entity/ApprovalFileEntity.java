package com.playdata.ElectronicApproval.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "approvalfile")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class ApprovalFileEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String name;
  private String text;
  private String approvalFormId;
  @Enumerated(EnumType.STRING)
  private ApprovalStatus status;

  @Enumerated(EnumType.STRING)
  private DeleteStatus deleteStatus;//delete_status

  @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean deleted;

  private String companyCode;

  private String employeeId;


  @OneToMany(mappedBy = "approvalFile", cascade = CascadeType.ALL)
  private List<ApprovalLineEntity> approvalLineEntities;


  @PrePersist
  protected void onCreate() {
    if (status == null) {
      this.status = ApprovalStatus.PENDING;
    }
    if (deleteStatus == null) {
      this.deleteStatus = DeleteStatus.ACTIVE;
    }
  }

}


