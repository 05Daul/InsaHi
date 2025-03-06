package com.playdata.attendanceSalary.atdSalDao.sal;

import com.playdata.attendanceSalary.atdSalEntity.sal.PositionEntity;
import com.playdata.attendanceSalary.atdSalRepository.sal.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.Position;

@Repository
@RequiredArgsConstructor
public class PositionDaoImpl implements PositionDao {
    private final PositionRepository positionRepository;
    @Override
    public void updatePosition(PositionEntity positionEntity,String CompanyCode) {
        positionRepository.save(positionEntity);
    }

    @Override
    public void deletePosition(PositionEntity positionEntity,String CompanyCode) {
        positionRepository.delete(positionEntity);
    }

    @Override
    public PositionEntity insertPosition(PositionEntity positionEntity,String CompanyCode) {
        return positionRepository.save(positionEntity);
    }

}
