package com.playdata.AttendanceSalary.atdSalDao.sal;

import com.playdata.AttendanceSalary.atdSalDto.sal.AddPersonPositionSalaryDTO;
import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionSalaryStepEntity;
import com.playdata.AttendanceSalary.atdSalRepository.sal.PositionSalaryStepRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PositionSalaryDaoImpl implements PositionSalaryDao {

  private final PositionSalaryStepRepository positionSalaryStepRepository;

  @Override
  public PositionSalaryStepEntity savePositionSalaryStep(
      PositionSalaryStepEntity positionSalaryStepEntity) {
    return positionSalaryStepRepository.save(positionSalaryStepEntity);
  }

  @Override
  public List<PositionSalaryStepEntity> findAllByCompanyCode(String companyCode) {
    return positionSalaryStepRepository.findAllByCompanyCode(companyCode);
  }

  @Override
  public void deletePosition(PositionSalaryStepEntity positionSalaryStepEntity) {
    positionSalaryStepRepository.delete(positionSalaryStepEntity);
  }

  @Override
  public Optional<PositionSalaryStepEntity> findPositionSalaryById(Long positionSalaryID) {
    return positionSalaryStepRepository.findByPositionSalaryId(positionSalaryID);
  }

  @Override
  public List<AddPersonPositionSalaryDTO> findAllPositionSalaryInfo(String companyCode) {
    return positionSalaryStepRepository.findAllPositionSalaryInfo(companyCode);
  }

}

