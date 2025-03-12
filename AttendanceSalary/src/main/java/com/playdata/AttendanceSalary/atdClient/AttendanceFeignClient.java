package com.playdata.attendanceSalary.atdClient;
import com.playdata.attendanceSalary.atdClient.hrmDTO.EmployeeResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "attendance-service", url = "${attendance.service.url")
public interface AttendanceFeignClient {

    @GetMapping("employ_id")
    EmployeeResponseDTO getEmployId(@RequestParam("employ_id") String employId);

}