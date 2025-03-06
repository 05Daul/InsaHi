package com.playdata.attendanceSalary.atdSalController.atd;

import com.playdata.User.company.entity.Company;
import com.playdata.attendanceSalary.atdSalEntity.atd.AttendanceEntity;
import com.playdata.attendanceSalary.atdSalService.atd.AttendanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * 내 출근시간이랑, 회사 출근시간 계산 다르개
 * 출근  버튼 , 퇴근
 * */
@Slf4j
@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService  attendanceService;

    @PostMapping("/checkin")
    public ResponseEntity<?> checkIn(@RequestParam("employeeId") String employeeId,
                                     @RequestParam("companyCode") String companyCode) throws Exception {
        AttendanceEntity attendance = attendanceService.checkIn(employeeId,companyCode);
        log.info("Controller:"+employeeId);
        System.out.println("attendance = " + attendance);
        log.info("Controller:"+ companyCode);
        return ResponseEntity.ok(attendance);

    }


    //  ResponseEntity<?> 으로 교체예정
    @PutMapping("/check-out/{id}")
    public void checkOut(@PathVariable("id") Long id) {
        attendanceService.checkOut(id);
    }
}