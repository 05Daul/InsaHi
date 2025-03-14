package com.playdata.AttendanceSalary.atdSalDao.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.AllowanceEntity;

import java.util.List;
import java.util.Optional;


public interface AllowanceDao {
    AllowanceEntity saveAllowance(AllowanceEntity allowanceEntity);
    Optional<AllowanceEntity> findAllowanceById(Long id);
    List<AllowanceEntity> findByCompanyCode(String companyCode);
    void deleteAllowanceById(Long id);
    List<AllowanceEntity> findByPayStubId(Long payStubId);




}
