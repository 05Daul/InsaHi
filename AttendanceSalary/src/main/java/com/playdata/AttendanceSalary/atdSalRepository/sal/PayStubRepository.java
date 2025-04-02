package com.playdata.AttendanceSalary.atdSalRepository.sal;

import com.playdata.AttendanceSalary.atdSalEntity.sal.PayStubEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PayStubRepository extends JpaRepository<PayStubEntity,Long> {
    PayStubEntity findByEmployeeId(@Param("employeeId") String employeeId);
    List<PayStubEntity> findAllByEmployeeId(@Param("employeeId") String employeeId);

    @Query("SELECT p FROM PayStubEntity p WHERE p.employeeId = :employeeId " +
            "AND FUNCTION('YEAR', p.paymentDate) = :year " +
            "AND FUNCTION('MONTH', p.paymentDate) = :month")
    List<PayStubEntity> findAllByEmployeeIdAndYearAndMonth(@Param("employeeId") String employeeId,
                                                           @Param("year") int year,
                                                           @Param("month")int month);
}
