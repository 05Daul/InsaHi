package com.playdata.AttendanceSalary.atdSalDao.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.PayStubEntity;
import com.playdata.AttendanceSalary.atdSalRepository.sal.PayStubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PayStubDaoImpl implements PayStubDao {
    private final PayStubRepository payStubRepository;

    @Override
    public PayStubEntity save(PayStubEntity payStubEntity) {
        return payStubRepository.save(payStubEntity);
    }

    @Override
    public void update(PayStubEntity payStubEntity) {
        payStubRepository.save(payStubEntity);
    }

    @Override
    public void delete(PayStubEntity payStubEntity) {
        payStubRepository.delete(payStubEntity);
    }

    @Override
    public PayStubEntity findPayStubByeEmployeeId(String employeeId) {
        return payStubRepository.findByEmployeeId(employeeId);
    }
}
