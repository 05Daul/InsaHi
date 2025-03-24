package com.playdata.AttendanceSalary.atdSalDao.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionSalaryStepEntity;

import java.util.List;
import java.util.Optional;

public interface PositionSalaryDao {

    PositionSalaryStepEntity savePositionSalaryStep(
            PositionSalaryStepEntity positionSalaryStepEntity);

    void deletePosition(PositionSalaryStepEntity positionSalaryStepEntity);

    Optional<PositionSalaryStepEntity> findPositionSalaryById(Long positionSalaryId);

    List<PositionSalaryStepEntity> findAllByCompanyCode(String companyCode);
}
