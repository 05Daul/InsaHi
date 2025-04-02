package com.playdata.AttendanceSalary.atdSalRepository.sal;

import com.playdata.AttendanceSalary.atdSalDto.sal.AddPersonPositionSalaryDTO;
import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionSalaryStepEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PositionSalaryStepRepository extends
    JpaRepository<PositionSalaryStepEntity, Long> {

  Optional<PositionSalaryStepEntity> findByPositionSalaryId(
      @Param("positionSalaryId") Long positionSalaryId);

  List<PositionSalaryStepEntity> findAllByCompanyCode(@Param("companyCode") String companyCode);

  @Query("SELECT new com.playdata.AttendanceSalary.atdSalDto.sal.AddPersonPositionSalaryDTO(" +
      "s.positionSalaryId, p.positionName, s.salaryStepId) " +
      "FROM PositionSalaryStepEntity s JOIN s.position p " +
      "WHERE s.companyCode = :companyCode")
  List<AddPersonPositionSalaryDTO> findAllPositionSalaryInfo(
      @Param("companyCode") String companyCode);

}
