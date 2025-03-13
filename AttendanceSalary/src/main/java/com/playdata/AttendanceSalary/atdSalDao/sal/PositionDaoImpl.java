package com.playdata.attendanceSalary.atdSalDao.sal;

import com.playdata.attendanceSalary.atdSalEntity.sal.PositionEntity;
import com.playdata.attendanceSalary.atdSalRepository.sal.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PositionDaoImpl implements PositionDao {
    private final PositionRepository positionRepository;

    @Override
    public void deletePosition(PositionEntity positionEntity) {
        positionRepository.delete(positionEntity);
    }

    @Override
    public PositionEntity savePosition(PositionEntity positionEntity) {
        return positionRepository.save(positionEntity);
    }

    @Override
    public Optional<PositionEntity> findById(Long positionId) {
        return positionRepository.findById(positionId);
    }
}
