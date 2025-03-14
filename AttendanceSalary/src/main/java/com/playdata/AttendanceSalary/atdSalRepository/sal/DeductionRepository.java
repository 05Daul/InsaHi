package com.playdata.AttendanceSalary.atdSalRepository.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.DeductionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeductionRepository extends JpaRepository<DeductionEntity,Long> {
    List<DeductionEntity> findByPayStubPayStubId(@Param("payStubId") Long payStubId);

}
