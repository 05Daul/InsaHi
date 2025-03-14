package com.playdata.AttendanceSalary.atdSalDto.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionEntity;
import lombok.Data;

@Data
public class PositionResponseDTO {
    private Long positionId;
    private String positionName;
    private String companyCode;

    public PositionEntity toEntity() {
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setPositionId(positionId);
        positionEntity.setPositionName(positionName);
        positionEntity.setCompanyCode(companyCode);
        return positionEntity;
    }
}
