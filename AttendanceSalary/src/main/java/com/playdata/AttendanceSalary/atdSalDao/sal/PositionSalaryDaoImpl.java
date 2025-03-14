package com.playdata.AttendanceSalary.atdSalDao.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionSalaryStepEntity;
import com.playdata.AttendanceSalary.atdSalRepository.sal.PositionSalaryStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PositionSalaryDaoImpl implements PositionSalaryDao {
    private final PositionSalaryStepRepository positionSalaryStepRepository;

    @Override
    public PositionSalaryStepEntity savePositionSalaryStep(PositionSalaryStepEntity positionSalaryStepEntity) {
    return positionSalaryStepRepository.save(positionSalaryStepEntity);
    }

    @Override
    public void deletePosition(PositionSalaryStepEntity positionSalaryStepEntity) {
        positionSalaryStepRepository.delete(positionSalaryStepEntity);
    }

    @Override
    public Optional<PositionSalaryStepEntity> findPositionSalaryById(Long positionSalaryID) {
        return positionSalaryStepRepository.findByPositionSalaryId(positionSalaryID);

    }
}

