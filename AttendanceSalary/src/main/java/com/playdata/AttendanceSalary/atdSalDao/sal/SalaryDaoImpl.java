//package com.playdata.attendanceSalary.atdSalDao.sal;
//
////import com.playdata.attendanceSalary.atdSalEntity.sal.SalaryEntity;
//import com.playdata.attendanceSalary.atdSalRepository.sal.SalaryRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//@RequiredArgsConstructor
//public class SalaryDaoImpl implements SalaryDao {
//
//    private final SalaryRepository salaryRepository;
//
//    @Override
//    public SalaryEntity saveSalary(SalaryEntity salaryEntity) {
//        return salaryRepository.save(salaryEntity);
//    }
//
//    @Override
//    public void deleteSalary(SalaryEntity SalaryEntity) {
//        salaryRepository.delete(SalaryEntity);
//    }
//
//    @Override
//    public Optional<SalaryEntity> findSalaryById(Long SalaryID) {
//        return salaryRepository.findById(SalaryID);
//    }
//}
