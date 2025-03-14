package com.playdata.AttendanceSalary.atdSalDao.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.EmployeeAllowEntity;

import java.util.List;
import java.util.Optional;

public interface EmployeeAllowDao {

    EmployeeAllowEntity save(EmployeeAllowEntity employeeAllowEntity);
    void update(EmployeeAllowEntity employeeAllowEntity);
    void delete(EmployeeAllowEntity employeeAllowEntity);
    Optional<EmployeeAllowEntity> selectByEmployeeAllowId(Long employeeAllowId);
    List<EmployeeAllowEntity> findByEmployeeId(String employeeId);


}
