package com.playdata.AttendanceSalary.atdSalDao.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionEntity;
import com.playdata.AttendanceSalary.atdSalRepository.sal.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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


    @Override
    public List<PositionEntity> findByCompanyCode(String companyCode) {
        return positionRepository.findAllByCompanyCode(companyCode);
    }
}
