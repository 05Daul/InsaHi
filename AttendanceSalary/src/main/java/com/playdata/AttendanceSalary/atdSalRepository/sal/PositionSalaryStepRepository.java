package com.playdata.AttendanceSalary.atdSalRepository.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionSalaryStepEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PositionSalaryStepRepository extends JpaRepository<PositionSalaryStepEntity, Long> {
    Optional<PositionSalaryStepEntity> findByPositionSalaryId(@Param("positionSalaryId") Long positionSalaryId);
    List<PositionSalaryStepEntity> findAllByCompanyCode(@Param("companyCode") String companyCode);
}
