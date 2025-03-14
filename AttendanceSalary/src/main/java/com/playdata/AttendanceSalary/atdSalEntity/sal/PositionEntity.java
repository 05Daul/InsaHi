package com.playdata.AttendanceSalary.atdSalEntity.sal;

import com.playdata.AttendanceSalary.atdSalDto.sal.PositionResponseDTO;
import com.playdata.common.publicEntity.DateEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "position")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionEntity extends DateEntity {
//직급
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long positionId;
    private String positionName;
    private String companyCode;


    public PositionResponseDTO toDTO(){
        PositionResponseDTO positionResponseDTO = new PositionResponseDTO();
        positionResponseDTO.setPositionId(this.positionId);
        positionResponseDTO.setPositionName(this.positionName);
        positionResponseDTO.setCompanyCode(this.companyCode);
        return positionResponseDTO;
    }

}
