package com.playdata.AttendanceSalary.atdSalDao.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.DeductionEntity;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DeductionDao{
    void deleteById(Long deductionId);
    DeductionEntity save(DeductionEntity deductionEntity);
    Optional<DeductionEntity> fetchById(Long deductionId);
    List<DeductionEntity> findByPayStubId(Long payStubId);
    void saveAll(List<DeductionEntity> deductions);
    BigDecimal sumByPayStubId(Long payStubId);

}
