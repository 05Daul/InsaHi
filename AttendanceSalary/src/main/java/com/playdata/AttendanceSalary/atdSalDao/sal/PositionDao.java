package com.playdata.attendanceSalary.atdSalDao.sal;

import com.playdata.attendanceSalary.atdSalEntity.sal.PositionEntity;
import org.springframework.stereotype.Repository;

import javax.swing.text.Position;

@Repository
public interface PositionDao { PositionEntity insertPosition(PositionEntity positionEntity,String CompanyCode);
void updatePosition(PositionEntity positionEntity,String CompanyCode);
void deletePosition(PositionEntity positionEntity,String CompanyCode);
}
