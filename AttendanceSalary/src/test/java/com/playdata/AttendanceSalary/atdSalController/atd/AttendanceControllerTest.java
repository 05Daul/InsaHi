package com.playdata.AttendanceSalary.atdSalController.atd;

import com.playdata.AttendanceSalary.atdSalEntity.atd.AttendanceEntity;
import com.playdata.AttendanceSalary.atdSalService.atd.AttendanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class AttendanceControllerTest {
    @Autowired
    private AttendanceService attendanceService;

    @Test
    public void checkIn() throws IllegalAccessException {
        String employeeId = "E001";
        String companyCode= "C001";

        AttendanceEntity attendance = attendanceService.checkIn(employeeId);

        System.out.println("attendance = " + companyCode);
        System.out.println("attendance = " + employeeId);
        System.out.println("attendance = " + attendance);

    }
}