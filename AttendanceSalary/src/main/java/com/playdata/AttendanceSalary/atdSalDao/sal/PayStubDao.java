package com.playdata.AttendanceSalary.atdSalDao.sal;
import com.playdata.AttendanceSalary.atdSalEntity.sal.PayStubEntity;

import java.util.List;

public interface PayStubDao  {
    PayStubEntity save(PayStubEntity payStubEntity);
    void update(PayStubEntity payStubEntity);
    void delete(PayStubEntity payStubEntity);
    PayStubEntity findPayStubByeEmployeeId(String employeeId);
    List<PayStubEntity> findAllPayStub(String employeeId);
    List<PayStubEntity> findByEmployeeIdAndYearMonth(String employeeId, int year, int month);

}
