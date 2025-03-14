package com.playdata.AttendanceSalary.atdSalDao.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.DeductionEntity;
import com.playdata.AttendanceSalary.atdSalRepository.sal.DeductionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class DeductionDaoImpl implements DeductionDao {
    private final DeductionRepository deductionRepository;

    @Override
    public void saveAll(List<DeductionEntity> deductions) {
        deductionRepository.saveAll(deductions);
    }

    @Override
    public void deleteById(Long deductionId) {
        deductionRepository.deleteById(deductionId);
    }

    @Override
    public DeductionEntity save(DeductionEntity deductionEntity) {
        return deductionRepository.save(deductionEntity);
    }

    @Override
    public Optional<DeductionEntity> fetchById(Long deductionId) {
        return deductionRepository.findById(deductionId);
    }

    @Override
    public List<DeductionEntity> findByPayStubId(Long payStubId) {
        return deductionRepository.findByPayStubPayStubId(payStubId);
    }
}
