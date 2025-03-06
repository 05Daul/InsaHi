package com.playdata.attendanceSalary.atdSalRepository.sal;

import com.playdata.attendanceSalary.atdSalEntity.sal.AllowanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface AllowanceRepository  extends JpaRepository<AllowanceEntity,Long> {
}
