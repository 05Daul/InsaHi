package com.playdata.AttendanceSalary.atdSalDao.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.EmployeeAllowEntity;
import com.playdata.AttendanceSalary.atdSalRepository.sal.EmployeeAllowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class EmployeeAllowDaoImpl implements EmployeeAllowDao {
    private final EmployeeAllowRepository employeeAllowRepository;


    @Override
    public List<EmployeeAllowEntity> findByEmployeeId(String employeeId) {
        return employeeAllowRepository.findByEmployeeId(employeeId);
    }

    @Override
    public EmployeeAllowEntity save(EmployeeAllowEntity employeeAllowEntity) {
        return employeeAllowRepository.save(employeeAllowEntity);
    }

    @Override
    public void update(EmployeeAllowEntity employeeAllowEntity) {
        employeeAllowRepository.save(employeeAllowEntity);
    }

    @Override
    public void delete(EmployeeAllowEntity employeeAllowEntity) {
        employeeAllowRepository.delete(employeeAllowEntity);
    }

    @Override
    public Optional<EmployeeAllowEntity> selectByEmployeeAllowId(Long employeeAllowId) {
        return employeeAllowRepository.findById(employeeAllowId);
    }

}
