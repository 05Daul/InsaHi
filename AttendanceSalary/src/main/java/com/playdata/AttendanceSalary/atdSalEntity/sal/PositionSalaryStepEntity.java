package com.playdata.AttendanceSalary.atdSalEntity.sal;

import com.playdata.common.publicEntity.DateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// 직급호봉
@Entity
@Table(name = "position_salary_step")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionSalaryStepEntity extends DateEntity {

  //직급 호봉 테이블
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "position_salary_id")
  private Long positionSalaryId; //직급 호봉 아이디
  @ManyToOne
  @JoinColumn(name = "position_id")
  private PositionEntity position;

  @Column(name = "salary_step_id")
  private int salaryStepId;

  @Column(precision = 11, scale = 2)
  private BigDecimal baseSalary;

  @Column(precision = 11, scale = 2)
  private BigDecimal positionAllowance;

  @Column(precision = 11, scale = 2)
  private BigDecimal overtimeAllowance;

  private int baseAnnualLeave;

  private String companyCode;

//    @ElementCollection employee에서 가지고 있기
//    private List<String> employees = new ArrayList<>();

}