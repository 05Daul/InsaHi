package com.playdata.attendanceSalary.atdSalRepository.sal;

import com.playdata.attendanceSalary.atdSalEntity.sal.DeductionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeductionRepository extends JpaRepository<DeductionEntity,Long> {
}
