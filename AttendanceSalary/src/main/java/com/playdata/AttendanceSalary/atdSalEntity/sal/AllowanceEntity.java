package com.playdata.AttendanceSalary.atdSalEntity.sal;

import com.playdata.AttendanceSalary.atdSalDto.sal.AllowanceResponseDTO;
import com.playdata.common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
//수당
@Builder
@Entity
@Table(name = "allowance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllowanceEntity extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long allowanceId;

    private String companyCode;

    @Enumerated(EnumType.STRING)
    private AllowanceType allowType;

    private BigDecimal allowSalary;

//    private Long salaryId;

    //private Long payStubId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_stub_id")
    private PayStubEntity payStub;
    // 비과세 여부와 한도는 AllowanceType으로 관리하므로 제거 가능
    // private boolean taxExemption;
    // private BigDecimal taxExemptionLimit;

    public AllowanceResponseDTO toDTO() {
        AllowanceResponseDTO dto = new AllowanceResponseDTO();
        dto.setAllowanceId(allowanceId);
        dto.setCompanyCode(companyCode);
        dto.setAllowName(allowType.getDisplayName());
        dto.setAllowSalary(allowSalary);
//        dto.setSalaryId(salaryId);
        return dto;
    }
}