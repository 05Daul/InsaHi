package com.playdata.AttendanceSalary.atdSalRepository.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.AllowanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public  interface AllowanceRepository  extends JpaRepository<AllowanceEntity,Long> {
    List<AllowanceEntity> findByPayStub_PayStubId(@Param("payStubId")Long payStubId);
    List<AllowanceEntity> findByCompanyCode(@Param("companyCode") String companyCode);
}
