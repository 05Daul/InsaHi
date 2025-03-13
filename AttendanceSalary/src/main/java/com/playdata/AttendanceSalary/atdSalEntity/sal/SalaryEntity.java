package com.playdata.attendanceSalary.atdSalEntity.sal;

import com.playdata.attendanceSalary.atdSalDto.sal.SalaryResponseDTO;
import com.playdata.common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "salary")
public class SalaryEntity  extends DateEntity {
    //급여항목
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salaryId;
    private char salaryType;
    private String description;
    private String companyCode;

    public SalaryResponseDTO toDTO(){
        SalaryResponseDTO salaryResponseDTO = new SalaryResponseDTO();
        salaryResponseDTO.setSalaryId(salaryId);
        salaryResponseDTO.setSalaryType(salaryType);
        salaryResponseDTO.setDescription(description);
        salaryResponseDTO.setCompanyCode(companyCode);
        return salaryResponseDTO;

    }

}
