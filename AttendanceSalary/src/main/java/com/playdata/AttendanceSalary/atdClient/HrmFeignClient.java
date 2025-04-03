package com.playdata.AttendanceSalary.atdClient;

import com.playdata.AttendanceSalary.atdClient.hrmDTO.CompanyStartTimeDto;
import com.playdata.AttendanceSalary.atdClient.hrmDTO.EmployeeResponseDTO;
import java.time.LocalTime;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "hrm-service", configuration = FeignClientConfig.class)
public interface HrmFeignClient {

  @GetMapping(value = "/employee/find", produces = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.TEXT_HTML_VALUE})
  EmployeeResponseDTO findEmployee(@RequestParam("employeeId") String employeeId);

  @GetMapping("/employee/{employeeId}/company/start-time")
  ResponseEntity<LocalTime> getCompanyStartTime(
      @PathVariable("employeeId") String employeeId);

  @GetMapping("/employee/getallemployeeids")
  List<String> getEmployeeIds();

  @PostMapping("/company/start-Time")
  CompanyStartTimeDto insertStartTime(@RequestBody CompanyStartTimeDto companyStartTimeDto);


}