package com.playdata.attendanceSalary.atdSalController.atd;

import com.playdata.attendanceSalary.atdSalEntity.atd.AttendanceEntity;
import com.playdata.attendanceSalary.atdSalService.atd.AttendanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class AttendanceControllerTest {
    @Autowired
    private AttendanceService attendanceService;

    @Test
    public void checkIn() throws IllegalAccessException {
        String employeeId = "E006";
        String companyCode= "C006";

        AttendanceEntity attendance = attendanceService.checkIn(employeeId, companyCode);

        System.out.println("attendance = " + companyCode);
        System.out.println("attendance = " + employeeId);
        System.out.println("attendance = " + attendance);

    }
}