package com.playdata.AttendanceSalary.atdClient;
import com.playdata.AttendanceSalary.atdClient.hrmDTO.EmployeeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;

@FeignClient(name = "hrm", url = "${hrm.service.url}")
public interface HrmFeignClient {

    @GetMapping("/admin/find")
    EmployeeResponseDTO findEmployee(@RequestParam("employeeId") String employeeId);

    @GetMapping("/admin/{employeeId}/company/start-time")
    public ResponseEntity<LocalTime> getCompanyStartTime(@PathVariable("employeeId") String employeeId);
}