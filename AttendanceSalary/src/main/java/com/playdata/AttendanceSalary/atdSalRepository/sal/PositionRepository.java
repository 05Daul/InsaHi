package com.playdata.AttendanceSalary.atdSalRepository.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.PositionEntity;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PositionRepository extends JpaRepository<PositionEntity, Long> {
    List<PositionEntity> findAllByCompanyCode(@Param("companyCode") String companyCode);
}
