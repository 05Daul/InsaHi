package com.playdata.attendanceSalary.atdSalService.sal;

import com.playdata.attendanceSalary.atdSalEntity.sal.PositionEntity;
import org.springframework.stereotype.Service;



public interface SalaryService {
    // 회사별 직급 생성
    PositionEntity insertPosition(PositionEntity positionEntity,String CompanyCode);
    void updatePosition(PositionEntity positionEntity,String CompanyCode);
    void deletePosition(PositionEntity positionEntity,String CompanyCode);

}
