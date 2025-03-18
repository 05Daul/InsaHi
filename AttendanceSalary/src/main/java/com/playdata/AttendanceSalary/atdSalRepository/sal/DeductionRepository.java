package com.playdata.AttendanceSalary.atdSalRepository.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.DeductionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

public interface DeductionRepository extends JpaRepository<DeductionEntity, Long> {
    List<DeductionEntity> findByPayStubPayStubId(@Param("payStubId") Long payStubId);

    @Query("SELECT COALESCE(SUM(d.amount), 0) FROM DeductionEntity d WHERE d.payStub.payStubId = :payStubId")
    BigDecimal sumByPayStubId(@Param("payStubId") Long payStubId);
}
