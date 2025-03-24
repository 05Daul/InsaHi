package com.playdata.AttendanceSalary.atdSalDao.sal;
import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionEntity;

import java.util.List;
import java.util.Optional;


public interface PositionDao {
    PositionEntity savePosition(PositionEntity positionEntity);

    void deletePosition(PositionEntity positionEntity);

    Optional<PositionEntity> findById(Long positionId);

    List<PositionEntity> findByCompanyCode(String companyCode);
}
