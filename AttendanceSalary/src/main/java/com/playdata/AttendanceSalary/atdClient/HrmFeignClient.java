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

  @GetMapping("/admin/find")
  EmployeeResponseDTO findEmployee(@RequestParam("employeeId") String employeeId);


  @GetMapping("/admin/{employeeId}/company/start-time")
  public ResponseEntity<LocalTime> getCompanyStartTime(
      @PathVariable("employeeId") String employeeId);

  @GetMapping("/admin/getallemployeeids")
  List<String> getEmployeeIds();

}