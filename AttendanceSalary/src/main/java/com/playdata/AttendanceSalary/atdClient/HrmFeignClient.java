package com.playdata.AttendanceSalary.atdClient;

import com.playdata.AttendanceSalary.atdClient.hrmDTO.EmployeeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;


@FeignClient(name = "hrm", url = "${hrm.service.url}")
public interface HrmFeignClient {

    @GetMapping("/employee/find")
    EmployeeResponseDTO findEmployee(@RequestParam("employeeId") String employeeId);

    @GetMapping("/employee/{employeeId}/company/start-time")
    ResponseEntity<LocalTime> getCompanyStartTime(
            @PathVariable("employeeId") String employeeId);

    @GetMapping("/employee/getallemployeeids")
    List<String> getEmployeeIds();

}