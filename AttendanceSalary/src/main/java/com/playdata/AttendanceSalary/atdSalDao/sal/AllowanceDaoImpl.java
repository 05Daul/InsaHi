package com.playdata.AttendanceSalary.atdSalDao.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.AllowanceEntity;
import com.playdata.AttendanceSalary.atdSalRepository.sal.AllowanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AllowanceDaoImpl implements AllowanceDao {

    private final AllowanceRepository allowanceRepository;

    @Override
    public AllowanceEntity saveAllowance(AllowanceEntity allowanceEntity) {
        allowanceRepository.save(allowanceEntity);
        return allowanceEntity;
    }

    @Override
    public List<AllowanceEntity> findByCompanyCode(String companyCode) {
         return allowanceRepository.findByCompanyCode(companyCode);
    }

    @Override
    public Optional<AllowanceEntity> findAllowanceById(Long id) {
        return allowanceRepository.findById(id);
    }

    @Override
    public void deleteAllowanceById(Long id) {
        allowanceRepository.deleteById(id);
    }

    @Override
    public List<AllowanceEntity> findByPayStubId(Long payStubId) {
        return allowanceRepository.findByPayStub_PayStubId(payStubId);

    }


}
