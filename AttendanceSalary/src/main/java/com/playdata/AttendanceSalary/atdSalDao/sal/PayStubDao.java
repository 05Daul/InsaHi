package com.playdata.AttendanceSalary.atdSalDao.sal;
import com.playdata.AttendanceSalary.atdSalEntity.sal.PayStubEntity;

public interface PayStubDao  {
    PayStubEntity save(PayStubEntity payStubEntity);
    void update(PayStubEntity payStubEntity);
    void delete(PayStubEntity payStubEntity);
    PayStubEntity findPayStubByeEmployeeId(String employeeId);
}
